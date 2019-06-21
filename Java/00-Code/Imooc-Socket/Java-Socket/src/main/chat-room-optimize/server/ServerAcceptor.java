package server;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.CountDownLatch;

import clink.utils.CloseUtils;

public class ServerAcceptor extends Thread {

    private final AcceptListener listener;
    private final Selector selector;
    private final CountDownLatch latch = new CountDownLatch(1);
    private boolean done = false;

    ServerAcceptor(AcceptListener listener) throws IOException {
        super("Server-Accept-Thread");
        this.listener = listener;
        this.selector = Selector.open();
    }

    boolean awaitRunning() {
        try {
            latch.await();
            return true;
        } catch (InterruptedException e) {
            return false;
        }
    }

    @Override
    public void run() {
        super.run();

        // 回调已进入运行
        latch.countDown();

        Selector selector = this.selector;
        do {
            try {
                if (selector.select() == 0) {
                    if (done) {
                        break;
                    }
                    continue;
                }

                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    if (done) {
                        break;
                    }

                    SelectionKey key = iterator.next();
                    iterator.remove();

                    // 检查当前Key的状态是否是我们关注的
                    // 客户端到达状态
                    if (key.isAcceptable()) {
                        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
                        // 非阻塞状态拿到客户端连接
                        SocketChannel socketChannel = serverSocketChannel.accept();
                        listener.onNewSocketArrived(socketChannel);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        } while (!done);

        System.out.println("ServerAcceptor Finished!");
    }

    void exit() {
        done = true;
        // 直接关闭
        CloseUtils.close(selector);
    }

    Selector getSelector() {
        return selector;
    }

    interface AcceptListener {
        void onNewSocketArrived(SocketChannel channel);
    }

}