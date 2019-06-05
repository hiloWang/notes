package clink.impl.async;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
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
public class AsyncSendDispatcher implements SendDispatcher, IoArgs.IoArgsEventProcessor {

    private Sender mSender;

    private AtomicBoolean mIsSending = new AtomicBoolean(false);
    private AtomicBoolean mIsClosed = new AtomicBoolean(false);
    private Queue<SendPacket> mSendPacketQueue = new ConcurrentLinkedQueue<>();

    private SendPacket<?> mSendingPacket;
    private ReadableByteChannel mSendingReadableByteChannel;

    private long mTotal;
    private int mPosition;

    private IoArgs mIoArgs = new IoArgs();

    public AsyncSendDispatcher(Sender sender) {
        mSender = sender;
        mSender.setSendListener(this);
    }

    @Override
    public void send(SendPacket packet) {
        //加入到队列中
        mSendPacketQueue.offer(packet);
        if (mIsSending.compareAndSet(false, true)) {
            sendNextMessage();
        }
    }

    private SendPacket takePacket() {
        SendPacket sendPacket = mSendPacketQueue.poll();
        //已经取消的包就不发送了
        if (sendPacket != null && sendPacket.isCanceled()) {
            return takePacket();
        }
        return sendPacket;
    }

    /*选取需要发送的下一个数据包*/
    private void sendNextMessage() {
        SendPacket sendingPacket = mSendingPacket;
        if (sendingPacket != null) {
            CloseUtils.close(sendingPacket);
        }

        SendPacket packet = mSendingPacket = takePacket();

        //队列为空，停止发送
        if (packet == null) {
            mIsSending.set(false);
            return;
        }

        mTotal = packet.getLength();
        mPosition = 0;
        //开始发送
        sendCurrentMessage();
    }

    private void closeAndNotify() {
        CloseUtils.close(this);
    }

    @Override
    public void cancel(SendPacket packet) {
    }

    @Override
    public void close() throws IOException {
        if (mIsClosed.compareAndSet(false, true)) {
            mIsSending.set(false);
            completePacket(false);
        }
    }

    private void sendCurrentMessage() {
        if (mPosition >= mTotal) {//写完了
            completePacket(mPosition == mTotal);
            sendNextMessage();
            return;
        }
        try {
            mSender.postSendAsync();
        } catch (IOException e) {
            e.printStackTrace();
            closeAndNotify();
        }
    }

    @Override
    public IoArgs provideIoArgs() {
        IoArgs ioArgs = mIoArgs;
        //用 mSendingReadableByteChannel 是否为 null 判断是否为新的包
        if (mSendingReadableByteChannel == null) {//新的包开始写，写头部
            mSendingReadableByteChannel = Channels.newChannel(mSendingPacket.open());
            ioArgs.limit(4);
            ioArgs.writeLength((int) mSendingPacket.getLength());
        } else {
            //开始写数据
            try {
                mIoArgs.limit((int) Math.min(mTotal - mPosition, mIoArgs.capacity()));
                //从 Channel 读取数据到 ioArgs 中
                int readCount = mIoArgs.readFrom(mSendingReadableByteChannel);
                mPosition += readCount;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
        return ioArgs;
    }

    @Override
    public void consumeFailed(IoArgs ioArgs, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onConsumeCompleted(IoArgs args) {
        /*写完了一个IoArgs，再继续写*/
        sendCurrentMessage();
    }

    /**
     * 完成Packet发送
     *
     * @param isSucceed 是否成功
     */
    private void completePacket(boolean isSucceed) {
        SendPacket packet = this.mSendingPacket;
        if (packet == null) {
            return;
        }

        CloseUtils.close(packet);
        CloseUtils.close(mSendingReadableByteChannel);

        mSendingPacket = null;
        mSendingReadableByteChannel = null;
        mTotal = 0;
        mPosition = 0;
    }


}
