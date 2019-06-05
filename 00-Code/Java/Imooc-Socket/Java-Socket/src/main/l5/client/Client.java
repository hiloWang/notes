package client;

import java.io.IOException;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/1 23:46
 */
class Client {

    public static void main(String... args) {
        //使用广播搜索服务器
        ServerInfo info = UDPSearcher.searchServer(10000);
        System.out.println("Server: " + info);

        if (info != null) {
            try {
                TCPClient.linkWith(info);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

}
