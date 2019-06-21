package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
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

    private final Socket mSocket;
    private final ClientReadHandler mReadHandler;
    private final ClientWriteHandler mWriteHandler;
    private final ClientHandlerCallback mClientHandlerCallback;
    private final String mClientInfo;

    ClientHandler(Socket client, ClientHandlerCallback clientHandlerCallback) throws IOException {
        mSocket = client;
        mClientHandlerCallback = Objects.requireNonNull(clientHandlerCallback);
        mReadHandler = new ClientReadHandler(client.getInputStream());
        mWriteHandler = new ClientWriteHandler(client.getOutputStream());
        mClientInfo = "A[" + client.getInetAddress().getHostAddress() + "] P[" + client.getPort() + "]";
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
        CloseUtils.close(mSocket);
        System.out.println("客户端已退出：" + mSocket.getInetAddress() + " P:" + mSocket.getPort());
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
        private final InputStream mInputStream;

        ClientReadHandler(InputStream inputStream) {
            mInputStream = inputStream;
        }

        @Override
        public void run() {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mInputStream));
            System.out.println("TCPServer start to read");
            String line;
            /*
             *一个死循环不断地读取
             * 是try cache包裹while，还是 while包裹try cache？当发生异常后不需要再继续循环时，就使用 try cache 包裹while，，否则  while 中使用 try cache 包裹执行代码。
             * 这里发生异常就表示连接断开了或者读取超时了，没有继续循环的必要。
             */
            try {

                while (!mDone) {
                    //客户端拿到数据
                    line = bufferedReader.readLine();
                    if (line == null) {//读取超时、socket异常、EOF时，str可能为null
                        System.out.println("客户端已无法读取数据！");
                        // 退出当前客户端
                        ClientHandler.this.exitBySelf();
                        break;
                    }
                    mClientHandlerCallback.onNewMessageArrived(ClientHandler.this, line);
                }

            } catch (IOException e) {
                e.printStackTrace();
                if (!mDone) {
                    System.out.println("连接异常断开");
                    ClientHandler.this.exitBySelf();
                }
            } finally {
                // 连接关闭
                CloseUtils.close(mInputStream);
            }
        }

        private void exit() {
            CloseUtils.close(mInputStream);
            mDone = true;
        }

    }//ClientReadHandler end

    /*负责向客户端写数据，内部实现为一个 SingleThreadExecutor*/
    private class ClientWriteHandler {

        private final PrintStream mPrintStream;
        private ExecutorService mExecutorService;
        private volatile boolean mDone;

        ClientWriteHandler(OutputStream outputStream) {
            mPrintStream = new PrintStream(outputStream);
            mExecutorService = Executors.newSingleThreadExecutor();
        }

        public void send(String line) {
            if (!mDone) {
                mExecutorService.execute(new SendRunnable(line));
            }
        }

        void exit() {
            mDone = true;
            CloseUtils.close(mPrintStream);
            mExecutorService.shutdownNow();
        }

        private class SendRunnable implements Runnable {

            private final String mMessage;

            private SendRunnable(String message) {
                mMessage = message;
            }

            @Override
            public void run() {
                if (mDone) {
                    return;
                }
                try {
                    mPrintStream.println(mMessage);
                    mPrintStream.flush();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }//SendRunnable end

    }//ClientWriteHandler end

}
