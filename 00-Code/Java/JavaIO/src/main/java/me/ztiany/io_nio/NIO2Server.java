package me.ztiany.io_nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;

public class NIO2Server {

    public static void main(String... args) throws IOException {

        InetSocketAddress sockAddr = new InetSocketAddress(9090);

        AsynchronousServerSocketChannel serverSock = AsynchronousServerSocketChannel.open().bind(sockAddr);

        serverSock.accept(serverSock, new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {
            // 为异步操作指定 CompletionHandler 回调函数
            @Override
            public void completed(AsynchronousSocketChannel sockChannel, AsynchronousServerSocketChannel serverSock) {
                serverSock.accept(serverSock, this);
                sayHelloWorld(sockChannel, Charset.defaultCharset().encode("Hello World!"));
            }

            @Override
            public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {

            }
        });
    }

    private static void sayHelloWorld(AsynchronousSocketChannel sockChannel, ByteBuffer encode) {

    }

}