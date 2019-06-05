package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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

    TCPServer(int portServer) {
        mPortServer = portServer;
        mClientHandlers = new ArrayList<>();
        mForwardingThreadPoolExecutor = Executors.newSingleThreadExecutor();
    }

    /*启动服务器*/
    boolean start() {
        try {
            ClientListener clientListener = new ClientListener(mPortServer);
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

        mForwardingThreadPoolExecutor.shutdownNow();
        mClientHandlers.clear();
        mClientListener.exit();
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

        private ServerSocket mServerSocket;
        private volatile boolean mDone;

        ClientListener(int port) throws IOException {
            mServerSocket = new ServerSocket(port);
            System.out.println("服务器信息：" + mServerSocket.getInetAddress() + " P:" + mServerSocket.getLocalPort());
        }

        @Override
        public void run() {
            System.out.println("服务器准备就绪～");
            /*
             *一个死循环不断地读取
             * 是try cache包裹while，还是 while包裹try cache？当发生异常后不需要再继续循环时，就使用 try cache 包裹 while，否则  while 中使用 try cache 包裹执行代码。
             * 这里发生异常没关系，表示接受这个连接时出错了，后面可以接收其他的连接
             */
            do {

                Socket client;

                try {
                    //等待客户端
                    client = mServerSocket.accept();
                } catch (IOException e) {
                    e.printStackTrace();
                    continue;
                }

                try {
                    ClientHandler clientHandler = new ClientHandler(client, TCPServer.this);
                    // 读取数据并打印
                    clientHandler.readToPrint();
                    synchronized ( TCPServer.this) {
                        mClientHandlers.add(clientHandler);
                    }
                } catch (IOException e) {
                    System.out.println("客户端连接异常：" + e.getMessage());
                }

            } while (!mDone);

            System.out.println("服务器已关闭！");
        }

        private void exit() {
            CloseUtils.close(mServerSocket);
            mDone = true;
        }

    }


}
