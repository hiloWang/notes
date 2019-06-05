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

    private IoArgs.IoArgsEventProcessor mReceiveIoEventListener;
    private IoArgs.IoArgsEventProcessor mSendIoEventListener;

    public SocketChannelAdapter(SocketChannel channel, IoProvider ioProvider, OnChannelStatusChangedListener onChannelStatusChangedListener) throws IOException {
        this.mChannel = channel;
        this.mIoProvider = ioProvider;
        this.mOnChannelStatusChangedListener = onChannelStatusChangedListener;
        this.mChannel.configureBlocking(false);
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

    @Override
    public boolean postSendAsync() throws IOException {
        //检查是否已经关闭
        checkState();
        //向 IoProvider 注册读回调，当可写时，mHandleOutputCallback 会被回调
        return mIoProvider.registerOutput(mChannel, mHandleOutputCallback);
    }

    @Override
    public void setSendListener(IoArgs.IoArgsEventProcessor ioArgsEventProcessor) {
        //保存 IO 事件监听器
        mSendIoEventListener = ioArgsEventProcessor;
    }

    private void checkState() throws IOException {
        if (mIsClosed.get()) {
            throw new IOException("Current channel is closed!");
        }
    }

    @Override
    public boolean postReceiveAsync() throws IOException {
        //检查是否已经关闭
        checkState();
        //向 IoProvider 注册读回调，当可读时，mHandleInputCallback 会被回调
        return mIoProvider.registerInput(mChannel, mHandleInputCallback);
    }

    @Override
    public void setReceiveListener(IoArgs.IoArgsEventProcessor ioArgsEventProcessor) {
        mReceiveIoEventListener = ioArgsEventProcessor;
    }

    public interface OnChannelStatusChangedListener {
        void onChannelClosed(SocketChannel channel);
    }

    /*当选择器选择对应的Channel可读时，将回调此接口*/
    private IoProvider.HandleInputCallback mHandleInputCallback = new IoProvider.HandleInputCallback() {

        @Override
        protected void canProviderInput() {
            if (mIsClosed.get()) {
                return;
            }

            IoArgs.IoArgsEventProcessor listener = mReceiveIoEventListener;

            //回调读取开始
            IoArgs ioArgs = listener.provideIoArgs();
            //具体的读取操作
            try {
                if (ioArgs == null) {//包是可以取消的，当取消一个包后，则提供的 ioArgs 为null。
                    listener.onConsumeFailed(null, new IOException("ProvideIoArgs is null."));
                } else if (ioArgs.readFrom(mChannel) > 0) {
                    // 读取完成回调
                    listener.onConsumeCompleted(ioArgs);
                } else {//Selector 选择后却又读不到数据，说明连接出问题了
                    listener.onConsumeFailed(ioArgs, new IOException("Cannot read any data!"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                CloseUtils.close(SocketChannelAdapter.this);
            }
        }
    };// mHandleInputCallback end

    /*当选择器选择对应的Channel可写时，将回调此接口*/
    private final IoProvider.HandleOutputCallback mHandleOutputCallback = new IoProvider.HandleOutputCallback() {
        @Override
        protected void canProviderOutput() {
            if (mIsClosed.get()) {
                return;
            }

            IoArgs.IoArgsEventProcessor listener = mSendIoEventListener;

            IoArgs ioArgs = listener.provideIoArgs();

            try {
                if (ioArgs == null) {
                    mSendIoEventListener.onConsumeFailed(null, new IOException("ProvideIoArgs is null."));
                } else if (ioArgs.writeTo(mChannel) > 0) {//具体的写操作
                    //此次写完回调回去，继续下一步操作
                    mSendIoEventListener.onConsumeCompleted(ioArgs);
                } else {//Selector 选择后却又写不出数据，说明连接出问题了
                    mSendIoEventListener.onConsumeFailed(ioArgs, new IOException("Cannot write any data!"));
                }
            } catch (IOException e) {
                e.printStackTrace();
                CloseUtils.close(SocketChannelAdapter.this);
            }

        }
    };//mHandleOutputCallback end

}