package clink.core;

import java.io.Closeable;
import java.nio.channels.SocketChannel;

/**
 * IO 读写调度器，可以把 关心可读/可写 的 SocketChannel 注册给 IoProvider ，当可读/可写时，对应的回调将会被调用。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/8 22:37
 */
public interface IoProvider extends Closeable {

    /**
     * 注册一个关心input的channel，当channel可读时，将会回调 callback
     */
    boolean registerInput(SocketChannel channel, HandleProviderCallback callback);

    /**
     * 注册一个关心output的channel，当channel可写时，将会回调 callback
     */
    boolean registerOutput(SocketChannel channel, HandleProviderCallback callback);

    void unRegisterInput(SocketChannel channel);

    void unRegisterOutput(SocketChannel channel);

    /**
     * 与关心可读/写的SocketChannel对应
     */
    abstract class HandleProviderCallback implements Runnable {
        /**
         * 附加本次未完全消费完成的IoArgs，然后进行自循环。
         */
        protected volatile IoArgs attach;

        @Override
        public final void run() {
            onProviderIo(attach);
        }

        /**
         * 当对应的 SocketChannel 可读/写时，此方法会被调用。
         */
        protected abstract void onProviderIo(IoArgs attach);

        /**
         * 检查当前的附加值是否未null，如果处于自循环时当前附加值不为null，
         * 此时如果外层有调度注册异步发送或者接收是错误的。
         */
        public void checkAttachNull() {
            if (attach != null) {
                throw new IllegalStateException("Current attach is not empty!");
            }
        }

    }


}
