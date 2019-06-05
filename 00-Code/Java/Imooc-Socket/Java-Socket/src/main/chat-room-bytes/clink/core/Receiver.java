package clink.core;

import java.io.Closeable;
import java.io.IOException;

/**
 * 一个数据接收者
 */
public interface Receiver extends Closeable {

    /**
     * 异步读取数据
     *
     * @param ioArgs 用于执行和保持读取数据的 IoArgs
     * @throws IOException 可能不可读
     */
    boolean receiveAsync(IoArgs ioArgs) throws IOException;

    /**
     * 设置接收监听
     *
     * @param ioArgsEventListener 监听器
     */
    void setReceiveCallback(IoArgs.IoArgsEventListener ioArgsEventListener);

}
