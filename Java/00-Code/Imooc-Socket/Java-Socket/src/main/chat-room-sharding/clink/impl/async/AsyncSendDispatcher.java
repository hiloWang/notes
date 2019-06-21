package clink.impl.async;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import clink.core.IoArgs;
import clink.core.SendDispatcher;
import clink.core.SendPacket;
import clink.core.Sender;
import clink.utils.CloseUtils;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/18 17:35
 */
public class AsyncSendDispatcher implements SendDispatcher, IoArgs.IoArgsEventProcessor, AsyncPacketReader.PacketProvider {

    private AtomicBoolean mIsSending = new AtomicBoolean(false);
    private AtomicBoolean mIsClosed = new AtomicBoolean(false);

    private Queue<SendPacket> mSendPacketQueue = new ConcurrentLinkedQueue<>();
    private final Object mQueueLock = new Object();

    private AsyncPacketReader mAsyncPacketReader = new AsyncPacketReader(this);

    private Sender mSender;

    public AsyncSendDispatcher(Sender sender) {
        mSender = sender;
        mSender.setSendListener(this);
    }

    @Override
    public void send(SendPacket packet) {
        //加入到队列中
        synchronized (mQueueLock) {
            mSendPacketQueue.offer(packet);
            //让AsyncPacketReader来请求一个包，如果返回true，则表示有数据需要发送
            if (mAsyncPacketReader.requestTakePacket()) {
                if (mIsSending.compareAndSet(false, true)) {
                    requestSend();//请求发送
                }
            }
        }
    }

    private void requestSend() {
        try {
            mSender.postSendAsync();
        } catch (IOException e) {
            e.printStackTrace();
            closeAndNotify();
        }
    }

    @Override
    public SendPacket takePacket() {
        SendPacket sendPacket;
        synchronized (mQueueLock) {
            sendPacket = mSendPacketQueue.poll();
            if (sendPacket == null) {
                mIsSending.set(false);
                return null;
            }
        }

        //已经取消的包就不发送了
        if (sendPacket.isCanceled()) {
            return takePacket();
        }

        return sendPacket;
    }


    private void closeAndNotify() {
        CloseUtils.close(this);
    }

    @Override
    public void cancel(SendPacket packet) {
        //完美取消
        boolean removed = mSendPacketQueue.remove(packet);
        if (removed) {
            packet.cancel();
            return;
        }
        //可能该包已经在发送了，调用包的发送者取消。
        mAsyncPacketReader.cancel(packet);
    }

    @Override
    public void close() throws IOException {
        if (mIsClosed.compareAndSet(false, true)) {
            mIsSending.set(false);
            mAsyncPacketReader.close();
        }
    }

    @Override
    public IoArgs provideIoArgs() {
        return mAsyncPacketReader.fillData();
    }

    @Override
    public void onConsumeFailed(IoArgs ioArgs, Exception e) {
        if (ioArgs == null) {
            e.printStackTrace();
        } else {
            //todo
        }
    }

    @Override
    public void onConsumeCompleted(IoArgs args) {
        /*写完了一个IoArgs，再继续写，重新注册*/
        if (mAsyncPacketReader.requestTakePacket()) {
            requestSend();
        }
    }

    /**
     * 完成Packet发送
     *
     * @param isSucceed 是否成功
     */
    @Override
    public void completedPacket(SendPacket sendPacket, boolean isSucceed) {
        CloseUtils.close(sendPacket);
    }

}
