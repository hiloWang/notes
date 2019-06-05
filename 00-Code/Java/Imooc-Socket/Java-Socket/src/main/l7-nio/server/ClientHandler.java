package server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import clink.CloseUtils;


/**
 * 用于处理客户端连接，读取客户端信息，向客户端发送消息。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/1 23:15
 */
class ClientHandler {

    private final SocketChannel mClient;
    private final ClientReadHandler mReadHandler;
    private final ClientWriteHandler mWriteHandler;
    private final ClientHandlerCallback mClientHandlerCallback;
    private final String mClientInfo;
    private final String newLineSign = System.getProperty("line.separator");
    private final int newLineLength = newLineSign.getBytes().length;

    ClientHandler(SocketChannel client, ClientHandlerCallback clientHandlerCallback) throws IOException {
        mClient = client;
        client.configureBlocking(false);
        mClientHandlerCallback = Objects.requireNonNull(clientHandlerCallback);

        Selector readSelector = Selector.open();
        mClient.register(readSelector, SelectionKey.OP_READ);

        Selector writeSelector = Selector.open();
        mClient.register(writeSelector, SelectionKey.OP_WRITE);

        mReadHandler = new ClientReadHandler(readSelector);
        mWriteHandler = new ClientWriteHandler(writeSelector);

        mClientInfo = mClient.getLocalAddress().toString();
        System.out.println("新客户端连接：" + mClientInfo);
    }

    String getClientInfo() {
        return mClientInfo;
    }

    void readToPrint() {
        mReadHandler.start();
    }

    public void send(String line) {
        mWriteHandler.send(line);
    }

    void exit() {
        mReadHandler.exit();
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

    /*从客户端读取数据*/
    private class ClientReadHandler extends Thread {

        private volatile boolean mDone;
        private final Selector mSelector;
        private ByteBuffer mByteBuffer;

        ClientReadHandler(Selector selector) {
            mSelector = selector;
            mByteBuffer = ByteBuffer.allocate(256);
        }

        @Override
        public void run() {
            System.out.println("TCPServer start to read");
            String line;
            try {

                while (!mDone) {

                    //阻塞等待可读
                    if (mSelector.select() == 0) {
                        if (mDone) {
                            break;
                        }
                        continue;
                    }

                    //处理可得的Socket
                    Iterator<SelectionKey> iterator = mSelector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        if (mDone) {
                            break;
                        }
                        SelectionKey next = iterator.next();
                        SocketChannel channel = (SocketChannel) next.channel();
                        //准备buffer
                        mByteBuffer.clear();
                        //开始读
                        int read = channel.read(mByteBuffer);
                        if (read > 0) {
                            line = new String(mByteBuffer.array(), 0, read - newLineLength /*-newLineLength 去掉换行符*/);
                            mClientHandlerCallback.onNewMessageArrived(ClientHandler.this, line);
                        } else {
                            System.out.println("客户端已无法读取数据！");
                            // 退出当前客户端
                            ClientHandler.this.exitBySelf();
                            break;
                        }
                        iterator.remove();
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
                if (!mDone) {
                    System.out.println("连接异常断开");
                    ClientHandler.this.exitBySelf();
                }
            } finally {
                // 连接关闭
                CloseUtils.close(mSelector);
            }
        }

        private void exit() {
            mDone = true;
            mSelector.wakeup();//唤醒一个，让其退出
            CloseUtils.close(mSelector);
        }

    }//ClientReadHandler end

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
                mMessage = message + newLineSign; /*加上换行符*/
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
