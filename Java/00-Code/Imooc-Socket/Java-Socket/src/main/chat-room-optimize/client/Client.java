package client;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import clink.box.FileSendPacket;
import clink.core.Connector;
import clink.core.IoContext;
import clink.core.ScheduleJob;
import clink.core.schedule.IdleTimeoutScheduleJob;
import clink.impl.IoSelectorProvider;
import clink.impl.SchedulerImpl;
import clink.utils.CloseUtils;
import foo.Foo;
import foo.handler.ConnectorCloseChain;
import foo.handler.ConnectorHandler;

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
                .scheduler(new SchedulerImpl(1))
                .start();
        //文件缓存路径
        File cachePath = Foo.getCacheDir("server");

        //使用广播搜索服务器
        ServerInfo info = UDPSearcher.searchServer(10000);
        System.out.println("Server: " + info);

        if (info != null) {
            TCPClient tcpClient = null;
            try {

                tcpClient = TCPClient.linkWith(info, cachePath);

                if (tcpClient != null) {

                    tcpClient.getCloseChain()
                            .appendLast(new ConnectorCloseChain() {
                                @Override
                                protected boolean consume(ConnectorHandler handler, Connector connector) {
                                    Runtime.getRuntime().exit(0);
                                    return true;
                                }
                            });

                    /*客户端和服务器，谁的超时时间短谁就能发送心跳*/
                    ScheduleJob scheduleJob = new IdleTimeoutScheduleJob(20, TimeUnit.SECONDS, tcpClient);
                    tcpClient.schedule(scheduleJob);

                    write(tcpClient);
                }
                System.out.println("client exit");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                CloseUtils.close(tcpClient);
            }
        }

        System.out.println("client exit");
        IoContext.close();
    }

    private static void write(TCPClient tcpClient) throws IOException {
        // 构建键盘输入流
        InputStream in = System.in;
        BufferedReader input = new BufferedReader(new InputStreamReader(in));

        do {
            // 键盘读取一行
            String str = input.readLine();
            System.out.println("-------str = "+str);
            if (str == null || Foo.COMMAND_EXIT.equalsIgnoreCase(str)) {
                break;
            }

            if (str.length() == 0) {
                continue;
            }

            //文件
            if (str.startsWith("--f ")) {
                String[] params = str.split(" ");
                if (params.length >= 2) {
                    File file = new File(params[1]);
                    if (file.exists()) {
                        tcpClient.send(new FileSendPacket(file));
                        continue;
                    } else {
                        System.out.println(file.getAbsolutePath() + " not exist");
                    }
                }
            }

            //字符串发送到服务器
            tcpClient.send(str);

        } while (true);
    }

}
