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
    boolean registerInput(SocketChannel channel, HandleInputCallback callback);

    /**
     * 注册一个关心output的channel，当channel可写时，将会回调 callback
     */
    boolean registerOutput(SocketChannel channel, HandleOutputCallback callback);

    void unRegisterInput(SocketChannel channel);

    void unRegisterOutput(SocketChannel channel);

    /**
     * 与关心可读的SocketChannel对应
     */
    abstract class HandleInputCallback implements Runnable {
        @Override
        public final void run() {
            canProviderInput();
        }

        protected abstract void canProviderInput();
    }

    /**
     * 与关心可写的SocketChannel对应
     */
    abstract class HandleOutputCallback implements Runnable {

        private Object attach;

        @Override
        public final void run() {
            canProviderOutput(attach);
        }

        /**
         * 用来保存需要写的数据，因为写不是立即执行的，该数据将会在 {@link #canProviderOutput(Object)}方法被调用时作为该方法的实参。
         */
        public final void setAttach(Object attach) {
            this.attach = attach;
        }

        /**
         * 当对应的 SocketChannel 可写时，此方法会被调用
         */
        protected abstract void canProviderOutput(Object attach);

        @SuppressWarnings("unchecked")
        public <T> T getAttach() {
            return (T) attach;
        }

    }

}
