package clink.core;

import java.io.Closeable;

/**
 * 接收数据包的调度者，把一份或多份 IoArgs 组合成一份 ReceivePack。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/18 16:42
 */
public interface ReceiveDispatcher extends Closeable {

    /**
     * 开始接收数据
     */
    void stop();

    /**
     * 停止接收数据
     */
    void start();

    /**
     * 数据接收的回调
     */
    interface ReceivePacketCallback {

        /**
         * 当完成一个数据包的接收时被调用
         */
        void onReceivePacketCompleted(ReceivePacket packet);

        /**
         * 根据类型和长度创建第一的 Packet
         */
        ReceivePacket<?, ?> onArrivedNewPacket(byte type, long length);

    }


}
