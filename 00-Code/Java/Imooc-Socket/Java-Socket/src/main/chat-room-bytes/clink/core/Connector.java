package clink.core;

import java.io.Closeable;
import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.UUID;

import clink.box.StringReceivePacket;
import clink.box.StringSendPacket;
import clink.impl.SocketChannelAdapter;
import clink.impl.async.AsyncReceiveDispatcher;
import clink.impl.async.AsyncSendDispatcher;

/**
 * 代表一个 SocketChannel 连接，用于调用 Sender 和  Receiver 执行读写操作。
 */
public class Connector implements Closeable, SocketChannelAdapter.OnChannelStatusChangedListener {

    /*该连接的唯一标识*/
    private UUID key = UUID.randomUUID();
    /*实际的连接*/
    private SocketChannel channel;

    /*数据发送者*/
    private Sender sender;
    /*数据接收者*/
    private Receiver receiver;

    private SendDispatcher sendDispatcher;
    private ReceiveDispatcher receiveDispatcher;

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
        //no op
    }

    public void send(String message) {
        sendDispatcher.send(new StringSendPacket(message));
    }

    @Override
    public void close() throws IOException {
        receiveDispatcher.close();
        sendDispatcher.close();
        sender.close();
        receiver.close();
        channel.close();
    }

    private ReceiveDispatcher.ReceivePacketCallback receivePacketCallback = packet -> {
        if (packet instanceof StringReceivePacket) {
            String message = packet.toString();
            onReceiveNewMessage(message);
        }
    };

    protected void onReceiveNewMessage(String newMessage) {
        System.out.println("Connector: " + key.toString() + ": " + newMessage);
    }

}