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

import clink.utils.CloseUtils;


/**
 * TCP 客户端，连接TCP服务器，发送消息。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/1 23:58
 */
class TCPClient {

    static void linkWith(ServerInfo info) throws IOException {
        Socket socket = null;
        ReadHandler readHandler = null;
        try {
            // 超时时间
            socket = new Socket();
            socket.setSoTimeout(3000/*读取超时*/);
            socket.connect(new InetSocketAddress(Inet4Address.getByName(info.getAddress()), info.getPort()), 3000/*连接超时*/);
            System.out.println("已发起服务器连接，并进入后续流程～");
            System.out.println("客户端信息：" + socket.getLocalAddress() + " P:" + socket.getLocalPort());
            System.out.println("服务器信息：" + socket.getInetAddress() + " P:" + socket.getPort());

            //启动读协程
            readHandler = new ReadHandler(socket);
            readHandler.start();

            //阻塞式写
            write(socket);

        } finally {
            //关闭
            if (readHandler != null) {
                readHandler.exit();
            }
            CloseUtils.close(socket);
        }
    }


    private static void write(Socket client) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        try (PrintStream printStream = new PrintStream(client.getOutputStream())) {
            String line;
            //todo 制造问题，发送多个消息，服务器会把这四个消息当成一个消息
            while (!(line = bufferedReader.readLine()).equalsIgnoreCase("00bye00")) {
                System.out.println("TCPClient send: " + line);
                printStream.println(line);
                printStream.println(line);
                printStream.println(line);
                printStream.println(line);
                printStream.flush();
            }
        }
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
