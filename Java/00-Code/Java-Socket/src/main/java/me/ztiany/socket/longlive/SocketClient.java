package me.ztiany.socket.longlive;

import java.io.Closeable;
import java.nio.charset.Charset;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/27 16:14
 */
class SocketClient implements Closeable {

    private LongLiveSocket mLongLiveSocket;

    SocketClient(String host, int port) {
        mLongLiveSocket = new LongLiveSocket(host, port,
                //on server data
                (data, offset, len) -> System.out.println("receive data: " + new String(data, offset, len)),
                //on error, return true to let mLongLiveSocket re initSocket
                () -> true);
    }

    void send(String line) {
        mLongLiveSocket.write(line.getBytes(Charset.forName("UTF8")), new LongLiveSocket.WritingCallback() {
            @Override
            public void onSuccess() {
                System.out.println("SocketClient send success");
            }

            @Override
            public void onFail(byte[] data, int offset, int len) {
                System.out.println("SocketClient send onFail and retry");
                mLongLiveSocket.write(data, offset, len, this);
            }
        });
    }

    @Override
    public void close() {
        if (mLongLiveSocket != null) {
            mLongLiveSocket.close();
        }
    }
}
