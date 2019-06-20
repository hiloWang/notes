package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import clink.core.IoContext;
import clink.impl.IoSelectorProvider;
import foo.TCPConstants;
import foo.UDPConstants;


/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/1 21:17
 */
class Server {

    public static void main(String... args) throws IOException {
        //启动IoContext
        IoContext.setup()
                .ioProvider(new IoSelectorProvider())
                .start();

        //启动 tcp 服务器
        TCPServer tcpServer = new TCPServer(TCPConstants.PORT_SERVER);
        boolean success = tcpServer.start();
        if (!success) {
            System.out.println("start TCPServer failed");
            return;
        }

        //启动 UDP 接受，让 TCP 服务可以通过 UDP 广播被搜索到
        UDPProvider.start(UDPConstants.PORT_SERVER);

        //读取键盘输入，发送给已连接的 tcp 客户端
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null;
        do {
            try {
                line = bufferedReader.readLine();
                if (!line.isEmpty()) {
                    tcpServer.broadcast(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } while (!"00bye00".equalsIgnoreCase(line));

        UDPProvider.stop();
        tcpServer.stop();
        //关闭IoContext
        IoContext.close();
    }

}
