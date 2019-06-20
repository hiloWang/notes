package client;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import clink.box.StringReceivePacket;
import clink.core.Packet;
import clink.core.ReceivePacket;
import clink.utils.CloseUtils;
import foo.handler.ConnectorHandler;
import foo.handler.ConnectorStringPacketChain;


/**
 * TCP 客户端，连接TCP服务器，发送消息。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/1 23:58
 */
class TCPClient extends ConnectorHandler {

    private TCPClient(SocketChannel client, File cachePath) throws IOException {
        super(client, cachePath);
        getStringPacketChain().appendLast(new PrintStringPacketChain());
    }

    private class PrintStringPacketChain extends ConnectorStringPacketChain {
        @Override
        protected boolean consume(ConnectorHandler handler, StringReceivePacket stringReceivePacket) {
            String str = stringReceivePacket.getEntity();
            System.out.println(str);
            return true;
        }
    }

    static TCPClient linkWith(ServerInfo info, File cachePath) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = SocketChannel.open();
            socketChannel.connect(new InetSocketAddress(Inet4Address.getByName(info.getAddress()), info.getPort()));
            System.out.println("已发起服务器连接，并进入后续流程～");
            System.out.println("客户端信息：" + socketChannel.getLocalAddress());
            System.out.println("服务器信息：" + socketChannel.getRemoteAddress());

            return new TCPClient(socketChannel, cachePath);
        } catch (IOException e) {
            System.out.println("连接异常");
            //关闭
            CloseUtils.close(socketChannel);
            return null;
        }
    }

    @Override
    protected void onReceiveNewPacket(ReceivePacket packet) {
        super.onReceiveNewPacket(packet);
        if (packet.getType() == Packet.TYPE_MEMORY_STRING) {
            System.out.println("receive: " + packet.getEntity());
        }
    }

}
