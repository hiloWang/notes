package client;

import java.io.File;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import clink.core.Connector;
import clink.core.Packet;
import clink.core.ReceivePacket;
import clink.utils.CloseUtils;
import foo.Foo;


/**
 * TCP 客户端，连接TCP服务器，发送消息。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/1 23:58
 */
class TCPClient extends Connector {

    private final File mCachePath;

    private TCPClient(SocketChannel socketChannel, File cachePath) throws IOException {
        setup(socketChannel);
        mCachePath = cachePath;
    }

    public void exit() {
        CloseUtils.close(this);
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
    protected File createNewReceiveFile() {
        return Foo.createRandomTemp(mCachePath);
    }

    @Override
    protected void onReceiveNewPacket(ReceivePacket packet) {
        super.onReceiveNewPacket(packet);
        if (packet.getType() == Packet.TYPE_MEMORY_STRING) {
            String string = (String) packet.getEntity();
            System.out.println(key.toString() + ":" + string);
        }
    }

}
