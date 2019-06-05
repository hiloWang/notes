package clink.impl;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicBoolean;

import clink.core.IoArgs;
import clink.core.IoProvider;
import clink.core.Receiver;
import clink.core.Sender;
import clink.utils.CloseUtils;

/**
 * SocketChannel 对读写的实现。
 */
public class SocketChannelAdapter implements Sender, Receiver, Cloneable {

    private final AtomicBoolean mIsClosed = new AtomicBoolean(false);
    private final SocketChannel mChannel;
    private final IoProvider mIoProvider;
    private final OnChannelStatusChangedListener mOnChannelStatusChangedListener;

    private IoArgs.IoArgsEventListener mReceiveIoEventListener;
    private IoArgs.IoArgsEventListener mSendIoEventListener;

    public SocketChannelAdapter(SocketChannel channel, IoProvider ioProvider, OnChannelStatusChangedListener onChannelStatusChangedListener) throws IOException {
        this.mChannel = channel;
        this.mIoProvider = ioProvider;
        this.mOnChannelStatusChangedListener = onChannelStatusChangedListener;
        this.mChannel.configureBlocking(false);
    }

    @Override
    public boolean receiveAsync(IoArgs.IoArgsEventListener listener) throws IOException {
        //检查是否已经关闭
        checkState();
        //保存 IO 事件监听器
        mReceiveIoEventListener = listener;
        //向 IoProvider 注册读回调，当可读时，mHandleInputCallback 会被回调
        return mIoProvider.registerInput(mChannel, mHandleInputCallback);
    }


    @Override
    public boolean sendAsync(IoArgs args, IoArgs.IoArgsEventListener listener) throws IOException {
        //检查是否已经关闭
        checkState();
        //保存 IO 事件监听器
        mSendIoEventListener = listener;
        // 当前发送的数据附加到回调中
        mHandleOutputCallback.setAttach(args);
        //向 IoProvider 注册读回调，当可写时，mHandleOutputCallback 会被回调
        return mIoProvider.registerOutput(mChannel, mHandleOutputCallback);
    }

    private void checkState() throws IOException {
        if (mIsClosed.get()) {
            throw new IOException("Current channel is closed!");
        }
    }

    @Override
    public void close() throws IOException {
        if (mIsClosed.compareAndSet(false, true)) {
            // 解除注册回调
            mIoProvider.unRegisterInput(mChannel);
            mIoProvider.unRegisterOutput(mChannel);
            // 关闭
            CloseUtils.close(mChannel);
            // 回调当前Channel已关闭
            mOnChannelStatusChangedListener.onChannelClosed(mChannel);
        }
    }

    private IoProvider.HandleInputCallback mHandleInputCallback = new IoProvider.HandleInputCallback() {

        @Override
        protected void canProviderInput() {
            if (mIsClosed.get()) {
                return;
            }
            IoArgs ioArgs = new IoArgs();
            IoArgs.IoArgsEventListener listener = mReceiveIoEventListener;
            if (listener != null) {
                listener.onStarted(ioArgs);
            }

            //具体的读取操作（假设一次就能读完）
            try {
                if (ioArgs.read(mChannel) > 0 && listener != null) {
                    // 读取完成回调
                    listener.onCompleted(ioArgs);
                } else {
                    throw new IOException("Cannot read any data!");
                }
            } catch (IOException e) {
                e.printStackTrace();
                CloseUtils.close(SocketChannelAdapter.this);
            }
        }

    };

    private final IoProvider.HandleOutputCallback mHandleOutputCallback = new IoProvider.HandleOutputCallback() {
        @Override
        protected void canProviderOutput(Object attach) {
            if (mIsClosed.get()) {
                return;
            }
            // TODO
            mSendIoEventListener.onCompleted(null);
        }
    };


    public interface OnChannelStatusChangedListener {
        void onChannelClosed(SocketChannel channel);
    }

}