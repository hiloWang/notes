package me.ztiany.socket.longlive;

import java.util.Scanner;

import static me.ztiany.socket.longlive.Utils.close;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/27 16:08
 */
public class LongLiveSocketMain {

    private static final int PORT = 8099;
    private EchoServer mEchoServer;

    public static void main(String... args) {
        LongLiveSocketMain longLiveSocketMain = new LongLiveSocketMain();
        longLiveSocketMain.startServer();
        longLiveSocketMain.startClient();
    }

    private void startClient() {
        SocketClient socketClient = null;
        Scanner scanner = null;
        try {
            socketClient = new SocketClient("localhost", PORT);
            scanner = new Scanner(System.in);
            String line;
            while (true) {
                line = scanner.nextLine();
                if (line.equalsIgnoreCase("q")) {
                    break;
                }
                socketClient.send(line);
            }

        } finally {
            close(scanner);
            close(socketClient);
            close(mEchoServer);
        }

    }

    private void startServer() {
        mEchoServer = new EchoServer(PORT);
        mEchoServer.start(new EchoServer.Callback() {
            @Override
            public void onSuccess() {
                System.out.println("服务器启成功");
            }

            @Override
            public void onError(Exception e) {
                System.out.println("服务器启失败：");
                e.printStackTrace();
            }
        });
    }

}
