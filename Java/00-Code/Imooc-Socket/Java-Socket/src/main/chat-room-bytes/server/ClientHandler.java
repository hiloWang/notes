package server;

import java.io.IOException;
import java.nio.channels.SocketChannel;
import java.util.Objects;

import clink.core.Connector;
import clink.utils.CloseUtils;


/**
 * 用于处理客户端连接，读取客户端信息，向客户端发送消息。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/1 23:15
 */
class ClientHandler extends Connector {

    private final ClientHandlerCallback mClientHandlerCallback;
    private final String mClientInfo;

    ClientHandler(SocketChannel client, ClientHandlerCallback clientHandlerCallback) throws IOException {
        //初始化客户端信息
        mClientInfo = client.getLocalAddress().toString();
        System.out.println("新客户端连接：" + mClientInfo);
        mClientHandlerCallback = Objects.requireNonNull(clientHandlerCallback);
        setup(client);
    }

    @Override
    public void onChannelClosed(SocketChannel channel) {
        super.onChannelClosed(channel);
        exitBySelf();
    }

    public String getClientInfo() {
        return mClientInfo;
    }

    @Override
    protected void onReceiveNewMessage(String newMessage) {
        super.onReceiveNewMessage(newMessage);
        mClientHandlerCallback.onNewMessageArrived(ClientHandler.this, newMessage);
    }

    void exit() {
        CloseUtils.close(this);
        System.out.println("客户端已退出：" + mClientInfo);
    }

    private void exitBySelf() {
        exit();
        mClientHandlerCallback.onSelfClosed(this);
    }

    interface ClientHandlerCallback {
        void onSelfClosed(ClientHandler clientHandler);

        void onNewMessageArrived(ClientHandler clientHandler, String message);
    }

}
