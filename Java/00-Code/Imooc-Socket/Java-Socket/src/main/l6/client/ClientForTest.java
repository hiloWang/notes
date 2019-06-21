package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/4 23:52
 */
public class ClientForTest {

    private volatile static boolean done;

    @SuppressWarnings("all")
    public static void main(String... args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        ServerInfo info = UDPSearcher.searchServer(10000);
        System.out.println("Server:" + info);
        if (info == null) {
            return;
        }

        // 当前连接数量
        int size = 0;
        final List<TCPClientForTest> tcpClients = new ArrayList<>();

        for (int i = 0; i < 500; i++) {

            TCPClientForTest tcpClient = TCPClientForTest.startWith(info);

            if (tcpClient == null) {
                System.out.println("连接异常");
                continue;
            }

            tcpClients.add(tcpClient);
            System.out.println("连接成功：" + (++size));

            try {
                //防止 TCP 连接队列超过 50 导致的异常。
                Thread.sleep(20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }//for end

        System.out.println("所有连接构建完成.......");

        bufferedReader.readLine();

        Runnable runnable = () -> {
            while (!done) {
                for (TCPClientForTest clientForTest : tcpClients) {
                    clientForTest.send("Hello~~");
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        System.out.println("已经开发发送数据.......");
        bufferedReader.readLine();
        System.out.println("开始进行关闭.......");

        done = true;

        // 等待线程完成
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 客户端结束操作
        for (TCPClientForTest clientForTest : tcpClients) {
            clientForTest.exit();
        }

        System.out.println("所有客户端已经退出.......");
    }


}
