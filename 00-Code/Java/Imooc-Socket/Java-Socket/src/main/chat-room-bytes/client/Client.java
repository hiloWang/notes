package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import clink.core.IoContext;
import clink.impl.IoSelectorProvider;
import clink.utils.CloseUtils;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/1 23:46
 */
class Client {

    public static void main(String... args) throws IOException {
        //启动IoContext
        IoContext.setup()
                .ioProvider(new IoSelectorProvider())
                .start();

        //使用广播搜索服务器
        ServerInfo info = UDPSearcher.searchServer(10000);
        System.out.println("Server: " + info);

        if (info != null) {
            TCPClient tcpClient = null;
            try {
                tcpClient = TCPClient.linkWith(info);
                if (tcpClient != null) {
                    write(tcpClient);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                CloseUtils.close(tcpClient);
            }
        }

        IoContext.close();
    }

    private static void write(TCPClient tcpClient) throws IOException {
        // 构建键盘输入流
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        do {
            // 键盘读取一行
            String str = input.readLine();
            // 发送到服务器
            tcpClient.send(str);
            tcpClient.send(str);
            tcpClient.send(str);
            tcpClient.send(str);
            if ("00bye00".equalsIgnoreCase(str)) {
                break;
            }
        } while (true);
    }

}
