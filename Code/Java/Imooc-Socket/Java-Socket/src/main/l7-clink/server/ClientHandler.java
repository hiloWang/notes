package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import clink.core.Connector;
import clink.utils.CharUtils;
import clink.utils.CloseUtils;


/**
 * 用于处理客户端连接，读取客户端信息，向客户端发送消息。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/1 23:15
 */
class ClientHandler {

    private final Connector mConnector;
    private final SocketChannel mClient;
    private final ClientHandlerCallback mClientHandlerCallback;
    private final ClientWriteHandler mWriteHandler;
    private final String mClientInfo;

    ClientHandler(SocketChannel client, ClientHandlerCallback clientHandlerCallback) throws IOException {
        mClient = client;
        mClientHandlerCallback = Objects.requireNonNull(clientHandlerCallback);

        //创建连接器并设置
        mConnector = new Connector() {
            @Override
            public void onChannelClosed(SocketChannel channel) {
                super.onChannelClosed(channel);
                exitBySelf();
            }

            @Override
            protected void onReceiveNewMessage(String newMessage) {
                super.onReceiveNewMessage(newMessage);
                mClientHandlerCallback.onNewMessageArrived(ClientHandler.this, newMessage);
            }
        };
        mConnector.setup(mClient);

        //写数据处理
        Selector writeSelector = Selector.open();
        mClient.register(writeSelector, SelectionKey.OP_WRITE);
        mWriteHandler = new ClientWriteHandler(writeSelector);
        //初始化客户端信息
        mClientInfo = mClient.getLocalAddress().toString();
        System.out.println("新客户端连接：" + mClientInfo);
    }

    String getClientInfo() {
        return mClientInfo;
    }

    public void send(String line) {
        mWriteHandler.send(line);
    }

    void exit() {
        CloseUtils.close(mConnector);
        mWriteHandler.exit();
        CloseUtils.close(mClient);
        System.out.println("客户端已退出：" + mClientInfo);
    }

    private void exitBySelf() {
        exit();
        mClientHandlerCallback.onSelfClosed(this);
    }

    interface ClientHandlerCallback {
        void onSelfClosed(ClientHandler clientHandler);

        void onNewMessageArrived(ClientHandler clientHandler, String message);
    }

    /*负责向客户端写数据，内部实现为一个 SingleThreadExecutor*/
    private class ClientWriteHandler {

        private final Selector mSelector;
        private ExecutorService mExecutorService;
        private volatile boolean mDone;
        private final ByteBuffer mByteBuffer;

        ClientWriteHandler(Selector selector) {
            mSelector = selector;
            mByteBuffer = ByteBuffer.allocate(256);
            mExecutorService = Executors.newSingleThreadExecutor();
        }

        public void send(String line) {
            if (!mDone) {
                mExecutorService.execute(new SendRunnable(line));
            }
        }

        void exit() {
            mDone = true;
            CloseUtils.close(mSelector);
            mExecutorService.shutdownNow();
        }

        private class SendRunnable implements Runnable {

            private final String mMessage;

            private SendRunnable(String message) {
                mMessage = message + CharUtils.LINE_BREAK; /*加上换行符*/
            }

            @Override
            public void run() {
                //提前结束
                if (mDone) {
                    return;
                }
                //准备Buffer
                mByteBuffer.clear();
                byte[] bytes = mMessage.getBytes();
                System.out.println("客户端：" + mClientInfo + " need write length = " + bytes.length);
                mByteBuffer.put(bytes);
                mByteBuffer.flip();//反正后，变为读模式limit = position，position=0
                try {
                    //开始写
                    while (!mDone && mByteBuffer.hasRemaining()) {
                        int write = mClient.write(mByteBuffer);
                        System.out.println("客户端：" + mClientInfo + " write length = " + write);
                        //len = 0 合法，存在不可写状态
                        if (write < 0) {
                            System.out.println("客户端已无法发送数据！");
                            ClientHandler.this.exitBySelf();
                            break;
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }//SendRunnable end

    }//ClientWriteHandler end

}
