package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import clink.CloseUtils;


/**
 * TCP 客户端，连接TCP服务器，发送消息。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/1 23:58
 */
class TCPClientForTest {

    private final ReadHandler mReadHandler;
    private final Socket mSocket;
    private final PrintStream mPrintStream;

    private TCPClientForTest(Socket socket, ReadHandler readHandler) throws IOException {
        mReadHandler = readHandler;
        mSocket = socket;
        mPrintStream = new PrintStream(mSocket.getOutputStream());
    }

    void exit() {
        mReadHandler.exit();
        CloseUtils.close(mPrintStream);
        CloseUtils.close(mSocket);
    }

    void send(String message) {
        System.out.println("发送---> " + message);
        mPrintStream.println(message);
    }

    static TCPClientForTest startWith(ServerInfo info) {
        Socket socket = new Socket();
        ReadHandler readHandler = null;

        try {
            socket.setSoTimeout(3000/*读取超时*/);
            socket.connect(new InetSocketAddress(Inet4Address.getByName(info.getAddress()), info.getPort()), 3000/*连接超时*/);

            System.out.println("已发起服务器连接，并进入后续流程～");
            System.out.println("客户端信息：" + socket.getLocalAddress() + " P:" + socket.getLocalPort());
            System.out.println("服务器信息：" + socket.getInetAddress() + " P:" + socket.getPort());

            //启动读协程
            readHandler = new ReadHandler(socket);
            readHandler.start();
            return new TCPClientForTest(socket, readHandler);

        } catch (Exception e) {
            CloseUtils.close(socket);
            if (readHandler != null) {
                readHandler.exit();
            }
        }

        return null;
    }

    static class ReadHandler extends Thread {

        private final InputStream mInputStream;
        private volatile boolean mDone = false;

        ReadHandler(Socket socket) throws IOException {
            mInputStream = socket.getInputStream();
        }

        @Override
        public void run() {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(mInputStream));
            String line;
            System.out.println("TCPClient start to read");

            try {

                while (!mDone) {
                    //如果是连接超时，则继续读取。
                    try {
                        line = bufferedReader.readLine();
                    } catch (SocketTimeoutException ignore) {
                        continue;
                    }

                    //如果读取的数据为null，则表示  EOF  了，无法再读取数据。
                    if (line == null) {
                        System.out.println("连接已关闭，无法读取数据！");
                        break;
                    }

                    // 打印到屏幕
                    System.out.println("收到服务器信息：" + line);
                }

            } catch (IOException e) {
                if (!mDone) {
                    System.out.println("连接异常断开：" + e.getMessage());
                }
            } finally {
                System.out.println("客户端连接异常后退出");
                exit();
            }

        }

        private void exit() {
            mDone = true;
            CloseUtils.close(mInputStream);
        }
    }//ReadHandler end

}
