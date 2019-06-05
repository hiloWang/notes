package clink.impl.async;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

import clink.core.IoArgs;
import clink.core.ReceiveDispatcher;
import clink.core.ReceivePacket;
import clink.core.Receiver;
import clink.utils.CloseUtils;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/18 17:00
 */
public class AsyncReceiveDispatcher implements ReceiveDispatcher, IoArgs.IoArgsEventProcessor, AsyncPacketWriter.PacketProvider {

    private AtomicBoolean mIsClosed = new AtomicBoolean(false);

    private Receiver mReceiver;
    private ReceivePacketCallback mReceivePacketCallback;
    private AsyncPacketWriter mAsyncPacketWriter = new AsyncPacketWriter(this);

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

    /**
     * 网络接收就绪，此时可以读取数据，需要返回一个容器用于容纳数据
     *
     * @return 用以容纳数据的IoArgs
     */
    @Override
    public IoArgs provideIoArgs() {
        return mAsyncPacketWriter.takeIoArgs();
    }

    @Override
    public void onConsumeFailed(IoArgs ioArgs, Exception e) {
        e.printStackTrace();
    }

    /**
     * 数据接收成功
     *
     * @param args IoArgs
     */
    @Override
    public void onConsumeCompleted(IoArgs args) {
        do {
            mAsyncPacketWriter.consumeIoArgs(args);
        } while (args.remained());
        //再次注册
        registerReceive();
    }

    private void closeAndNotify() {
        CloseUtils.close(this);
    }

    @Override
    public void close() throws IOException {
        if (mIsClosed.compareAndSet(false, true)) {
            mAsyncPacketWriter.close();
        }
    }

    /**
     * 构建Packet操作，根据类型、长度构建一份用于接收数据的Packet
     */
    @Override
    public ReceivePacket takePacket(byte type, long length, byte[] headerInfo) {
        return mReceivePacketCallback.onArrivedNewPacket(type, length);
    }

    @Override
    public void completedPacket(ReceivePacket packet, boolean isSucceed) {
        CloseUtils.close(packet);
        mReceivePacketCallback.onReceivePacketCompleted(packet);
    }


}
