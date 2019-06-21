package server;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import clink.box.StringReceivePacket;
import clink.core.Connector;
import clink.core.ScheduleJob;
import clink.core.schedule.IdleTimeoutScheduleJob;
import clink.utils.CloseUtils;
import foo.Foo;
import foo.handler.ConnectorHandler;
import foo.handler.ConnectorCloseChain;
import foo.handler.ConnectorStringPacketChain;


/**
 * TCP 服务器，用于获取客户端连接，创建 ClientHandler 来处理客户端连接。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/1 21:17
 */
class TCPServer implements ServerAcceptor.AcceptListener, Group.GroupMessageAdapter {

    private final int mPortServer;
    private final List<ConnectorHandler> mConnectorHandlers = new ArrayList<>();
    private final ExecutorService mDeliveryPool;
    private final File mCachePath;//文件缓存路径
    private ServerAcceptor mAcceptor;//用于处理客户端连接
    private ServerSocketChannel mServerSocketChannel;
    private final Map<String, Group> groups = new HashMap<>();

    private final ServerStatistics mStatistics = new ServerStatistics();

    TCPServer(int portServer, File cachePath) {
        mPortServer = portServer;
        mCachePath = cachePath;
        mDeliveryPool = Executors.newSingleThreadExecutor();
        //创建一个群
        this.groups.put(Foo.DEFAULT_GROUP_NAME, new Group(Foo.DEFAULT_GROUP_NAME, this));
    }

    /*启动服务器*/
    boolean start() {
        try {
            ServerAcceptor clientListener = new ServerAcceptor(this);

            mServerSocketChannel = ServerSocketChannel.open();
            mServerSocketChannel.configureBlocking(false);//配置非阻塞
            mServerSocketChannel.bind(new InetSocketAddress(mPortServer));
            mServerSocketChannel.register(clientListener.getSelector(), SelectionKey.OP_ACCEPT);

            System.out.println("服务器信息：" + mServerSocketChannel.getLocalAddress());

            clientListener.start();
            mAcceptor = clientListener;

            if (mAcceptor.awaitRunning()) {
                System.out.println("服务器准备就绪～");
                System.out.println("服务器信息：" + mServerSocketChannel.getLocalAddress().toString());
                return true;
            } else {
                System.out.println("启动异常！");
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*给所有客户端发送消息*/
    void broadcast(String line) {
        line = "系统通知：" + line;
        ConnectorHandler[] connectorHandlers;
        synchronized (mConnectorHandlers) {
            connectorHandlers = mConnectorHandlers.toArray(new ConnectorHandler[0]);
        }
        for (ConnectorHandler connectorHandler : connectorHandlers) {
            connectorHandler.send(line);
        }
    }

    /*停止服务器*/
    void stop() {
        if (mAcceptor != null) {
            mAcceptor.exit();
        }

        ConnectorHandler[] connectorHandlers;
        synchronized (mConnectorHandlers) {
            connectorHandlers = mConnectorHandlers.toArray(new ConnectorHandler[0]);
            mConnectorHandlers.clear();
        }

        for (ConnectorHandler connectorHandler : connectorHandlers) {
            connectorHandler.exit();
        }

        mDeliveryPool.shutdownNow();

        CloseUtils.close(mServerSocketChannel);
    }

    /**
     * 获取当前的状态信息
     */
    Object[] getStatusString() {
        return new String[]{
                "客户端数量：" + mConnectorHandlers.size(),
                "发送数量：" + mStatistics.sendSize,
                "接收数量：" + mStatistics.receiveSize
        };
    }

    @Override
    public void onNewSocketArrived(SocketChannel channel) {
        try {
            ConnectorHandler connectorHandler = new ConnectorHandler(channel, mCachePath);
            System.out.println(connectorHandler.getClientInfo() + ":Connected!");

            // 添加收到消息的处理责任链
            connectorHandler.getStringPacketChain()
                    .appendLast(mStatistics.statisticsChain())
                    .appendLast(new ParseCommandConnectorStringPacketChain());

            // 添加关闭链接时的责任链
            connectorHandler.getCloseChain()
                    .appendLast(new RemoveQueueOnConnectorClosedChain());

            /*客户端和服务器，谁的超时时间短谁就能发送心跳*/
            ScheduleJob scheduleJob = new IdleTimeoutScheduleJob(10, TimeUnit.SECONDS, connectorHandler);
            connectorHandler.schedule(scheduleJob);

            synchronized (mConnectorHandlers) {
                mConnectorHandlers.add(connectorHandler);
                System.out.println("当前客户端数量：" + mConnectorHandlers.size());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("客户端链接异常：" + e.getMessage());
        }
    }

    @Override
    public void sendMessageToClient(ConnectorHandler handler, String msg) {
        handler.send(msg);
        mStatistics.sendSize++;
    }

    /*用于处理连接关闭*/
    private class RemoveQueueOnConnectorClosedChain extends ConnectorCloseChain {

        @Override
        protected boolean consume(ConnectorHandler handler, Connector connector) {
            synchronized (mConnectorHandlers) {
                mConnectorHandlers.remove(handler);
            }
            // 移除群聊的客户端
            Group group = groups.get(Foo.DEFAULT_GROUP_NAME);
            group.removeMember(handler);
            return true;
        }

    }

    /*用于处理通过String发送的命令*/
    private class ParseCommandConnectorStringPacketChain extends ConnectorStringPacketChain {

        @Override
        protected boolean consume(ConnectorHandler handler, StringReceivePacket stringReceivePacket) {
            String entity = stringReceivePacket.getEntity();
            switch (entity) {
                case Foo.COMMAND_GROUP_JOIN: {
                    Group group = groups.get(Foo.DEFAULT_GROUP_NAME);
                    if (group.addMember(handler)) {
                        sendMessageToClient(handler, "Join Group:" + group.getName());
                    }
                    return true;
                }
                case Foo.COMMAND_GROUP_LEAVE: {
                    Group group = groups.get(Foo.DEFAULT_GROUP_NAME);
                    if (group.removeMember(handler)) {
                        sendMessageToClient(handler, "Leave Group:" + group.getName());
                    }
                    return true;
                }
            }
            return false;
        }

        @Override
        protected boolean consumeAgain(ConnectorHandler handler, StringReceivePacket stringReceivePacket) {
            // 捡漏的模式，当我们第一遍未消费，然后又没有加入到群，自然没有后续的节点消费
            // 此时我们进行二次消费，返回发送过来的消息
            sendMessageToClient(handler, stringReceivePacket.getEntity());
            return true;
        }

    }


}
