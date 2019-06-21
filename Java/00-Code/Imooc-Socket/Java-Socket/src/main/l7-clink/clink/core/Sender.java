package clink.core;

import java.io.Closeable;
import java.io.IOException;

/**
 * 一个数据发送者
 */
public interface Sender extends Closeable {

    /**
     * 异步发送数据
     *
     * @param args 需要发送的数据
     * @param listener 数据发送事件回调
     * @return
     * @throws IOException 可能不可写
     */
    boolean sendAsync(IoArgs args, IoArgs.IoArgsEventListener listener) throws IOException;

}
