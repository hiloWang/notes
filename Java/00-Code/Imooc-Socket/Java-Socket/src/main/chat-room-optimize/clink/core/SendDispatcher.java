package clink.core;

import java.io.Closeable;

/**
 * 发送数据的调度者，缓存所有需要发送的数据，通过队列控制数据发送，并在数据发送时，实现对数据的基本包装。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/18 16:39
 */
public interface SendDispatcher extends Closeable {

    /**
     * 发送一份数据
     *
     * @param packet 数据
     */
    void send(SendPacket packet);

    /**
     * 取消发送数据
     *
     * @param packet 数据
     */
    void cancel(SendPacket packet);

    /**
     * 发送一个心跳包
     */
    void sendHeartbeat();

}
