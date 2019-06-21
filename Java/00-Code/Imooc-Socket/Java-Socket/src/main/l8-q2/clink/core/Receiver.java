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
     * @param listener 数据读取事件回调
     * @return
     * @throws IOException 可能不可读
     */
    boolean receiveAsync(IoArgs.IoArgsEventListener listener) throws IOException;

}
