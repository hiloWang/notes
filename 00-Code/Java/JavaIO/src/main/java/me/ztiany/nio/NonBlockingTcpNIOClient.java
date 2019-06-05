package me.ztiany.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Scanner;

public class NonBlockingTcpNIOClient {

    //客户端
    public static void main(String... args) throws IOException {
        //1. 获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));
        System.out.println("connected---->");
        //2. 切换非阻塞模式
        sChannel.configureBlocking(false);
        //3. 分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);
        //4. 发送数据给服务端
        Scanner scan = new Scanner(System.in);

        while (scan.hasNext()) {
            String str = scan.next();
            System.out.println("read---->" + str);
            if (str.equals("q")) {
                break;
            }
            buf.put((new Date().toString() + "\n" + str).getBytes());
            buf.flip();
            while (buf.hasRemaining()) {
                sChannel.write(buf);
            }
            buf.clear();
        }
        //5. 关闭通道
        sChannel.close();
    }

}
