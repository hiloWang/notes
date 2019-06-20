package clink.impl.async;

import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.concurrent.atomic.AtomicBoolean;

import clink.core.IoArgs;
import clink.core.Packet;
import clink.core.ReceiveDispatcher;
import clink.core.ReceivePacket;
import clink.core.Receiver;
import clink.utils.CloseUtils;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/18 17:00
 */
public class AsyncReceiveDispatcher implements ReceiveDispatcher, IoArgs.IoArgsEventProcessor {

    private AtomicBoolean mIsClosed = new AtomicBoolean(false);

    private Receiver mReceiver;
    private ReceivePacketCallback mReceivePacketCallback;

    private IoArgs mIoArgs = new IoArgs();
    private ReceivePacket<?, ?> mPacketTemp;
    private WritableByteChannel mWritableByteChannelTemp;

    private int mTotal;
    private long mPosition;

    public AsyncReceiveDispatcher(Receiver receiver, ReceivePacketCallback receivePacketCallback) {
        mReceiver = receiver;
        mReceiver.setReceiveListener(this);
        mReceivePacketCallback = receivePacketCallback;
    }

    @Override
    public void stop() {
    }

    @Override
    public void start() {
        registerReceive();
    }

    private void registerReceive() {
        try {
            mReceiver.postReceiveAsync();
        } catch (IOException e) {
            e.printStackTrace();
            closeAndNotify();
        }
    }

    /*解析包*/
    private void assemblePacket(IoArgs args) {
        //是不是是一条新的消息
        if (mPacketTemp == null) {
            int length = args.readLength();
            byte type = length >= 200 ? Packet.TYPE_STREAM_FILE : Packet.TYPE_MEMORY_STRING;
            //根据包长度需求，创建一个StringReceivePacket
            mPacketTemp = mReceivePacketCallback.onArrivedNewPacket(type, length);
            //使用 packet 的流创建一个 Channel
            mWritableByteChannelTemp = Channels.newChannel(mPacketTemp.open());
            //初始化容器和位置标识
            mTotal = length;
            mPosition = 0;
        }

        try {
            //写入到我们的 mWritableByteChannel 中
            int readCount = args.writeTo(mWritableByteChannelTemp);
            mPosition += readCount;
            // 检查是否已完成一份Packet接收
            if (mPosition == mTotal) {
                completePacket(true);
                mPacketTemp = null;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 完成数据接收操作
     *
     * @param isSuccess 是否成功
     */
    private void completePacket(boolean isSuccess) {
        ReceivePacket receivePacket = mPacketTemp;
        CloseUtils.close(mPacketTemp);
        mPacketTemp = null;

        CloseUtils.close(mWritableByteChannelTemp);
        mWritableByteChannelTemp = null;

        if (receivePacket != null) {
            mReceivePacketCallback.onReceivePacketCompleted(receivePacket);
        }
    }

    @Override
    public IoArgs provideIoArgs() {
        IoArgs args = mIoArgs;
        int receiveSize;
        if (mPacketTemp == null) {//说明是一个新的消息的读取，先获取长度
            receiveSize = 4;//按照约定，用4个字节表示长度
        } else {//说明还是读取之前没有读完的消息，则接收的长度应该是，总长度-以读取的长度，同时还要考虑 args 的容量
            receiveSize = (int) Math.min(mTotal - mPosition, args.capacity());
        }
        //设置本次接收数据的长度
        args.limit(receiveSize);
        return args;
    }

    @Override
    public void consumeFailed(IoArgs ioArgs, Exception e) {
        e.printStackTrace();
    }

    @Override
    public void onConsumeCompleted(IoArgs args) {
        //完成了单次（非阻塞）接收，则解析包
        assemblePacket(args);
        //然后继续接收下一个数据包
        registerReceive();
    }

    private void closeAndNotify() {
        CloseUtils.close(this);
    }

    @Override
    public void close() throws IOException {
        if (mIsClosed.compareAndSet(false, true)) {
            completePacket(false);
        }
    }

}
