package client;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import clink.core.Connector;
import clink.utils.CloseUtils;


/**
 * TCP 客户端，连接TCP服务器，发送消息。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/1 23:58
 */
class TCPClient extends Connector {

    public TCPClient(SocketChannel socketChannel) throws IOException {
        setup(socketChannel);
    }

    public void exit() {
        CloseUtils.close(this);
    }

    static TCPClient linkWith(ServerInfo info) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(Inet4Address.getByName(info.getAddress()), info.getPort()));
            System.out.println("已发起服务器连接，并进入后续流程～");
            System.out.println("客户端信息：" + socketChannel.getLocalAddress());
            System.out.println("服务器信息：" + socketChannel.getRemoteAddress());

            return new TCPClient(socketChannel);
        } catch (IOException e) {
            System.out.println("连接异常");
            //关闭
            CloseUtils.close(socketChannel);
            return null;
        }
    }

}
