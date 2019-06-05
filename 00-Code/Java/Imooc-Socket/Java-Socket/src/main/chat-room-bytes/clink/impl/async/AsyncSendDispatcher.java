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
public class AsyncSendDispatcher implements SendDispatcher {

    private Sender mSender;

    private AtomicBoolean mIsSending = new AtomicBoolean(false);
    private AtomicBoolean mIsClosed = new AtomicBoolean(false);
    private Queue<SendPacket> mSendPacketQueue = new ConcurrentLinkedQueue<>();

    private SendPacket mSendingPacket;
    private int mTotal;
    private int mPosition;

    private IoArgs mIoArgs = new IoArgs();

    public AsyncSendDispatcher(Sender sender) {
        mSender = sender;
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
        System.out.println("AsyncSendDispatcher.sendNextMessage mTotal = " + mTotal + " mPosition = " + mPosition);

        //开始发送
        sendCurrentMessage();
    }

    private void sendCurrentMessage() {
        IoArgs ioArgs = mIoArgs;

        //清理、复位
        mIoArgs.startWriting();

        if (mPosition >= mTotal) {//写完了
            sendNextMessage();
            return;
        } else if (mPosition == 0) {//写头部
            ioArgs.writeLength(mSendingPacket.getLength());
        }

        //开始写数据
        byte[] bytes = mSendingPacket.bytes();
        //从bytes读取数据到ioArgs中
        int readCount = ioArgs.readFrom(bytes, mPosition);
        mPosition += readCount;

        //完成封装
        mIoArgs.finishWriting();

        try {
            mSender.sendAsync(mIoArgs, mIoArgsEventListener);
        } catch (IOException e) {
            e.printStackTrace();
            closeAndNotify();
        }

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

            SendPacket sendingPacket = mSendingPacket;
            if (sendingPacket != null) {
                CloseUtils.close(sendingPacket);
                mSendingPacket = null;
            }
        }
    }

    private IoArgs.IoArgsEventListener mIoArgsEventListener = new IoArgs.IoArgsEventListener() {
        @Override
        public void onStarted(IoArgs args) {

        }

        @Override
        public void onCompleted(IoArgs args) {
            /*写完了一个IoArgs，再继续写*/
            sendCurrentMessage();
        }
    };


}
