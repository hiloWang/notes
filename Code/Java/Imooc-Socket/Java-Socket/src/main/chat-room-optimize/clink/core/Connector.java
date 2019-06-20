package clink.core;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import clink.box.BytesReceivePacket;
import clink.box.FileReceivePacket;
import clink.box.StringReceivePacket;
import clink.box.StringSendPacket;
import clink.impl.SocketChannelAdapter;
import clink.impl.async.AsyncReceiveDispatcher;
import clink.impl.async.AsyncSendDispatcher;
import clink.utils.CloseUtils;

/**
 * 代表一个 SocketChannel 连接，用于调用 Sender 和  Receiver 执行读写操作。
 */
public abstract class Connector implements Closeable, SocketChannelAdapter.OnChannelStatusChangedListener {

    /*该连接的唯一标识*/
    protected final UUID key = UUID.randomUUID();
    /*实际的连接*/
    private SocketChannel channel;

    /*数据发送者*/
    private Sender sender;
    /*数据接收者*/
    private Receiver receiver;

    private SendDispatcher sendDispatcher;
    private ReceiveDispatcher receiveDispatcher;
    private final List<ScheduleJob> mScheduleJobs = new ArrayList<>(4);

    public void setup(SocketChannel socketChannel) throws IOException {
        this.channel = socketChannel;

        IoContext ioContext = IoContext.get();
        SocketChannelAdapter socketChannelAdapter = new SocketChannelAdapter(channel, ioContext.getIoProvider(), this);

        this.sender = socketChannelAdapter;
        this.receiver = socketChannelAdapter;

        sendDispatcher = new AsyncSendDispatcher(sender);
        receiveDispatcher = new AsyncReceiveDispatcher(receiver, receivePacketCallback);

        // 启动接收
        receiveDispatcher.start();
    }

    @Override
    public void onChannelClosed(SocketChannel channel) {
        CloseUtils.close(this);
    }

    public void send(String message) {
        if (message == null) {
            return;
        }
        sendDispatcher.send(new StringSendPacket(message));
    }

    public void send(SendPacket packet) {
        sendDispatcher.send(packet);
    }

    public void schedule(ScheduleJob scheduleJob) {
        synchronized (mScheduleJobs) {
            if (mScheduleJobs.contains(scheduleJob)) {
                return;
            }
            Scheduler scheduler = IoContext.get().scheduler();
            scheduleJob.schedule(scheduler);
            mScheduleJobs.add(scheduleJob);
        }
    }

    /**
     * 发射一份空闲超时事件
     */
    public void fireIdleTimeoutEvent() {
        sendDispatcher.sendHeartbeat();
    }

    /**
     * 发射一份异常事件，子类需要关注
     *
     * @param throwable 异常
     */
    public void fireExceptionCaught(Throwable throwable) {
    }

    /**
     * 获取最后的活跃时间点
     *
     * @return 发送、接收的最后活跃时间
     */
    public long getLastActiveTime() {
        return Math.max(sender.getLastWriteTime(), receiver.getLastReadTime());
    }

    @Override
    public void close() throws IOException {
        synchronized (mScheduleJobs) {
            // 全部取消调度
            for (ScheduleJob scheduleJob : mScheduleJobs) {
                scheduleJob.unSchedule();
            }
            mScheduleJobs.clear();
        }
        receiveDispatcher.close();
        sendDispatcher.close();
        sender.close();
        receiver.close();
        channel.close();
    }

    private ReceiveDispatcher.ReceivePacketCallback receivePacketCallback = new ReceiveDispatcher.ReceivePacketCallback() {
        @Override
        public void onReceivePacketCompleted(ReceivePacket packet) {
            onReceiveNewPacket(packet);
        }

        @Override
        public ReceivePacket<?, ?> onArrivedNewPacket(byte type, long length) {
            switch (type) {
                case Packet.TYPE_MEMORY_BYTES:
                    return new BytesReceivePacket(length);
                case Packet.TYPE_MEMORY_STRING:
                    return new StringReceivePacket(length);
                case Packet.TYPE_STREAM_FILE:
                    return new FileReceivePacket(length, createNewReceiveFile());
                case Packet.TYPE_STREAM_DIRECT:
                    return new BytesReceivePacket(length);
                default:
                    throw new UnsupportedOperationException("Unsupported packet type:" + type);
            }
        }

        @Override
        public void onReceivedHeartbeat() {
            System.out.println(key + ": [Heartbeat]");
        }
    };

    protected abstract File createNewReceiveFile();

    protected void onReceiveNewPacket(ReceivePacket packet) {
        System.out.println(key.toString() + " : [New Packet]-Type : " + packet.getType() + ", Length:" + packet.getLength());
    }

    public UUID getKey() {
        return key;
    }

}