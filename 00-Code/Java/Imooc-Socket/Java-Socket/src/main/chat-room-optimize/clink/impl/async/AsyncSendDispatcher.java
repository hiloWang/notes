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
public class AsyncSendDispatcher implements SendDispatcher, IoArgs.IoArgsEventProcessor,
        AsyncPacketReader.PacketProvider {

    private final AtomicBoolean mIsSending = new AtomicBoolean(false);
    private final AtomicBoolean mIsClosed = new AtomicBoolean(false);

    private final Queue<SendPacket> mSendPacketQueue = new ConcurrentLinkedQueue<>();

    private final AsyncPacketReader mAsyncPacketReader = new AsyncPacketReader(this);

    private final Sender mSender;

    public AsyncSendDispatcher(Sender sender) {
        mSender = sender;
        mSender.setSendListener(this);
    }

    /**
     * 发送Packet：首先添加到队列，如果当前状态为未启动发送状态，则尝试让reader提取一份packet进行数据发送，
     * 如果提取数据后reader有数据，则进行异步输出注册。
     *
     * @param packet 数据
     */
    @Override
    public void send(SendPacket packet) {
        //加入到队列中
        mSendPacketQueue.offer(packet);
        //请求发送
        requestSend();
    }

    /**
     * 发送心跳帧，将心跳帧放到帧发送队列进行发送
     */
    @Override
    public void sendHeartbeat() {
        //如果队列中有实际的业务数据，则没有必要发送心跳帧。
        if (mSendPacketQueue.size() > 0) {
            return;
        }
        if (mAsyncPacketReader.requestSendHeartbeatFrame()) {
            requestSend();
        }
    }


    /**
     * 取消Packet操作：如果还在队列中，代表Packet未进行发送，则直接标志取消，并返回即可，
     * 如果未在队列中，则让reader尝试扫描当前发送序列，查询是否当前Packet正在发送，
     * 如果是则进行取消相关操作。
     *
     * @param packet 数据
     */
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
    public SendPacket takePacket() {
        SendPacket sendPacket = mSendPacketQueue.poll();
        //没有包就返回null，停止发送。
        if (sendPacket == null) {
            return null;
        }
        //已经取消的包就不发送了
        if (sendPacket.isCanceled()) {
            return takePacket();
        }
        return sendPacket;
    }

    /*
    对于一个连接器内部的发送调度者，只有由一个线程将其从未发送状态置为发送中的状态，且进行这种状态转换的同时，只注册一次发送请求。
    也就是说，对一个 frame 完成一次完整的写操作，是单个线程的，只有这个写操作完成，才会注册下一次发送请求。
    */
    private void requestSend() {
        synchronized (mIsSending) {
            if (mIsSending.get() || mIsClosed.get()) {
                return;
            }
            // 返回True代表当前有数据需要发送
            if (mAsyncPacketReader.requestTakePacket()) {
                try {
                    //必须保证 postSendAsync 只被调用一次。
                    boolean success = mSender.postSendAsync();
                    if (success) {
                        mIsSending.set(true);
                    }
                } catch (IOException e) {
                    closeAndNotify();
                }
            }
        }
    }

    /**
     * 请求网络发送异常时触发，进行关闭操作
     */
    private void closeAndNotify() {
        CloseUtils.close(this);
    }

    /**
     * 关闭操作，关闭自己同时需要关闭reader
     */
    @Override
    public void close() {
        if (mIsClosed.compareAndSet(false, true)) {
            mAsyncPacketReader.close();
            mSendPacketQueue.clear();
            synchronized (mIsSending) {
                mIsSending.set(false);
            }
        }
    }

    /**
     * 网络发送就绪回调，当前已进入发送就绪状态，等待填充数据进行发送，
     * 此时从reader中填充数据，并进行后续网络发送。
     *
     * @return NULL，可能填充异常，或者想要取消本次发送
     */
    @Override
    public IoArgs provideIoArgs() {
        //System.out.println(Thread.currentThread()+" provideIoArgs "+this);
        return mIsClosed.get() ? null : mAsyncPacketReader.fillData();
    }

    @Override
    public void onConsumeFailed(IoArgs ioArgs, Exception e) {
        //System.out.println(Thread.currentThread()+" onConsumeFailed "+this);

        e.printStackTrace();
        // 设置当前发送状态
        synchronized (mIsSending) {
            mIsSending.set(false);
        }
        // 继续请求发送当前的数据
        requestSend();
    }

    @Override
    public void onConsumeCompleted(IoArgs args) {
        //System.out.println(Thread.currentThread()+" onConsumeCompleted "+this);

        // 设置当前发送状态
        synchronized (mIsSending) {
            mIsSending.set(false);
        }
        // 继续请求发送当前的数据
        requestSend();
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
