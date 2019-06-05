package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import clink.CloseUtils;

/**
 * TCP 服务器，用于获取客户端连接，创建 ClientHandler 来处理客户端连接。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/1 21:17
 */
class TCPServer implements ClientHandler.ClientHandlerCallback {

    private final int mPortServer;
    private final List<ClientHandler> mClientHandlers;
    private final ExecutorService mForwardingThreadPoolExecutor;
    private ClientListener mClientListener;
    private Selector mSelector;
    private ServerSocketChannel mServerSocketChannel;

    TCPServer(int portServer) {
        mPortServer = portServer;
        mClientHandlers = new ArrayList<>();
        mForwardingThreadPoolExecutor = Executors.newSingleThreadExecutor();
    }

    /*启动服务器*/
    boolean start() {
        try {
            mSelector = Selector.open();
            mServerSocketChannel = ServerSocketChannel.open();
            mServerSocketChannel.configureBlocking(false);//配置非阻塞
            mServerSocketChannel.bind(new InetSocketAddress(mPortServer));
            mServerSocketChannel.register(mSelector, SelectionKey.OP_ACCEPT);

            System.out.println("服务器信息：" + mServerSocketChannel.getLocalAddress());

            ClientListener clientListener = new ClientListener();
            clientListener.start();
            mClientListener = clientListener;
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /*给所有客户端发送消息*/
    void broadcast(String line) {
        synchronized (this) {
            System.out.println("TCPServer send: " + line + " to clients: " + mClientHandlers.size());
            for (ClientHandler clientHandler : mClientHandlers) {
                clientHandler.send(line);
            }
        }
    }

    /*停止服务器*/
    void stop() {

        synchronized (this) {
            for (ClientHandler clientHandler : mClientHandlers) {
                clientHandler.exit();
            }
        }
        mClientHandlers.clear();
        mClientListener.exit();

        mForwardingThreadPoolExecutor.shutdownNow();

        CloseUtils.close(mServerSocketChannel);
        CloseUtils.close(mSelector);
    }

    @Override
    public synchronized void onSelfClosed(ClientHandler clientHandler) {
        mClientHandlers.remove(clientHandler);
    }

    @Override
    public synchronized void onNewMessageArrived(ClientHandler clientHandler, String message) {
        final String senderClient = clientHandler.getClientInfo();
        System.out.println("收到客户端：" + senderClient + " 信息： " + message);
        mForwardingThreadPoolExecutor.execute(() -> {
            synchronized (TCPServer.this) {
                for (ClientHandler handler : mClientHandlers) {
                    if (handler.getClientInfo().equals(senderClient)) {
                        continue;
                    }
                    handler.send(message);
                }
            }
        });
    }

    /**
     * TCP监听
     */
    private class ClientListener extends Thread {

        private volatile boolean mDone;

        ClientListener() {
        }

        @Override
        public void run() {
            System.out.println("服务器准备就绪～");
            Selector selector = mSelector;

            do {

                try {

                    //阻塞等待连接
                    if (selector.select() == 0) {
                        if (mDone) {
                            break;
                        }
                        continue;
                    }

                    //处理已经准备好的连接
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        if (mDone) {
                            //可以提前退出
                            break;
                        }
                        SelectionKey selectionKey = iterator.next();
                        if (selectionKey.isAcceptable()) {
                            //这个Channel就是上面注册 ACCEPT 的 ServerSocketChannel
                            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
                            // 非阻塞状态拿到客户端连接
                            SocketChannel socketChannel = serverSocketChannel.accept();
                            //创建 ClientHandler 处理客户端读写
                            try {
                                ClientHandler clientHandler = new ClientHandler(socketChannel, TCPServer.this);
                                clientHandler.readToPrint();
                                synchronized (TCPServer.this) {
                                    mClientHandlers.add(clientHandler);
                                }
                            } catch (IOException e) {
                                //抓住异常，继续处理下一个。
                                System.out.println("客户端连接异常：" + e.getMessage());
                            }
                        }
                        //处理完一个key就要移除它
                        iterator.remove();
                    }

                } catch (IOException ignore/*这个用来处理 Selector.select()*/) {

                }

            } while (!mDone);

            System.out.println("服务器已关闭！");
        }

        private void exit() {
            mDone = true;
            // 使尚未返回的第一个选择操作立即返回。
            //如果另一个线程目前正阻塞在 select() 或 select(long) 方法的调用中，则该调用将立即返回。
            mSelector.wakeup();
        }

    }


}
