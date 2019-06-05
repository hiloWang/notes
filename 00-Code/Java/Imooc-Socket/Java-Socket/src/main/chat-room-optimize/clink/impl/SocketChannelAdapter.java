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

    // 最后活跃时间点
    private volatile long mLastReadTime = System.currentTimeMillis();
    private volatile long mLastWriteTime = System.currentTimeMillis();

    public SocketChannelAdapter(SocketChannel channel, IoProvider ioProvider, OnChannelStatusChangedListener onChannelStatusChangedListener) throws IOException {
        this.mChannel = channel;
        this.mIoProvider = ioProvider;
        this.mOnChannelStatusChangedListener = onChannelStatusChangedListener;
        this.mChannel.configureBlocking(false);
    }

    @Override
    public void close() {
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
        // 进行Callback状态监测，判断是否处于自循环状态
        mHandleOutputCallback.checkAttachNull();
        //向 IoProvider 注册读回调，当可写时，mHandleOutputCallback 会被回调
        return mIoProvider.registerOutput(mChannel, mHandleOutputCallback);
    }

    @Override
    public void setSendListener(IoArgs.IoArgsEventProcessor ioArgsEventProcessor) {
        //保存 IO 事件监听器
        mSendIoEventListener = ioArgsEventProcessor;
    }

    @Override
    public long getLastWriteTime() {
        return mLastWriteTime;
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
        // 进行Callback状态监测，判断是否处于自循环状态
        mHandleInputCallback.checkAttachNull();
        //向 IoProvider 注册读回调，当可读时，mHandleInputCallback 会被回调
        return mIoProvider.registerInput(mChannel, mHandleInputCallback);
    }

    @Override
    public void setReceiveListener(IoArgs.IoArgsEventProcessor ioArgsEventProcessor) {
        mReceiveIoEventListener = ioArgsEventProcessor;
    }

    @Override
    public long getLastReadTime() {
        return mLastReadTime;
    }

    public interface OnChannelStatusChangedListener {
        void onChannelClosed(SocketChannel channel);
    }

    /*当选择器选择对应的Channel可读时，将回调此接口*/
    private IoProvider.HandleProviderCallback mHandleInputCallback = new IoProvider.HandleProviderCallback() {

        @Override
        protected void onProviderIo(IoArgs args) {
            if (mIsClosed.get()) {
                return;
            }

            mLastReadTime = System.currentTimeMillis();

            IoArgs.IoArgsEventProcessor listener = mReceiveIoEventListener;

            if (args == null) {
                args = listener.provideIoArgs();
            }

            //回调读取开始，具体的读取操作
            try {
                if (args == null) {//包是可以取消的，当取消一个包后，则提供的 ioArgs 为null。
                    listener.onConsumeFailed(null, new IOException("ProvideIoArgs is null."));
                } else {
                    int count = args.readFrom(mChannel);

                    if (count == 0) {
                        // 本次回调就代表可以进行数据消费，
                        // 但是如果一个数据也没有产生消费，那么我们尝试输出一句语句到控制台
                        System.out.println("Current write zero data!");
                    }

                    if (args.remained()) {
                        //没有读完，下次再读
                        attach = args;
                        mIoProvider.registerOutput(mChannel, this);
                    } else {
                        //读完置为null
                        attach = null;
                        // 读取完成回调
                        listener.onConsumeCompleted(args);
                    }

                }
            } catch (IOException ignore) {
                CloseUtils.close(SocketChannelAdapter.this);
            }
        }
    };// mHandleInputCallback end

    /*当选择器选择对应的Channel可写时，将回调此接口*/
    private final IoProvider.HandleProviderCallback mHandleOutputCallback = new IoProvider.HandleProviderCallback() {

        @Override
        protected void onProviderIo(IoArgs args) {
            if (mIsClosed.get()) {
                return;
            }

            mLastWriteTime = System.currentTimeMillis();

            IoArgs.IoArgsEventProcessor listener = mSendIoEventListener;

            if (args == null) {
                args = listener.provideIoArgs();
            }

            try {
                if (args == null) {
                    mSendIoEventListener.onConsumeFailed(null, new IOException("ProvideIoArgs is null."));
                } else {

                    int count = args.writeTo(mChannel);

                    if (count == 0) {
                        // 本次回调就代表可以进行数据消费，
                        // 但是如果一个数据也没有产生消费，那么我们尝试输出一句语句到控制台
                        System.out.println("Current read zero data!");
                    }

                    if (args.remained()) {
                        // 附加当前未消费完成的args
                        attach = args;
                        // 再次注册数据发送
                        mIoProvider.registerInput(mChannel, this);
                    } else {
                        // 设置为null
                        attach = null;
                        // 读取完成回调
                        //此次写完回调回去，继续下一步操作
                        mSendIoEventListener.onConsumeCompleted(args);
                    }
                }
            } catch (IOException e) {
                CloseUtils.close(SocketChannelAdapter.this);
            }
        }
    };//mHandleOutputCallback end

}

