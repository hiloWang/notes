package server;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import clink.core.IoContext;
import clink.impl.IoSelectorProvider;
import clink.impl.SchedulerImpl;
import foo.Foo;
import foo.FooGui;
import foo.constants.TCPConstants;
import foo.constants.UDPConstants;


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
                .scheduler(new SchedulerImpl(1))
                .start();

        //文件缓存路径
        File cachePath = Foo.getCacheDir("server");

        //启动 tcp 服务器
        TCPServer tcpServer = new TCPServer(TCPConstants.PORT_SERVER, cachePath);
        boolean success = tcpServer.start();
        if (!success) {
            System.out.println("start TCPServer failed");
            return;
        }

        //启动 UDP 接受，让 TCP 服务可以通过 UDP 广播被搜索到
        UDPProvider.start(UDPConstants.PORT_SERVER);

        // 启动Gui界面
        FooGui gui = new FooGui("Clink-Server", tcpServer::getStatusString);
        gui.doShow();


        //读取键盘输入，发送给已连接的 tcp 客户端
        InputStreamReader inputStreamReader = new InputStreamReader(System.in);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line;
        do {

            line = bufferedReader.readLine();
            if (line == null || Foo.COMMAND_EXIT.equalsIgnoreCase(line)) {
                break;
            }
            if (line.length() == 0) {
                continue;
            }
            // 发送字符串
            tcpServer.broadcast(line);

        } while (true);

        UDPProvider.stop();
        tcpServer.stop();
        //关闭IoContext
        IoContext.close();
        gui.doDismiss();
    }

}
