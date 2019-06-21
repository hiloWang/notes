package clink.box;

import java.io.ByteArrayOutputStream;

import clink.core.ReceivePacket;

/**
 * 定义最基础的基于{@link ByteArrayOutputStream}的输出接收包
 *
 * @param <Entity> 对应的实体范性，需定义{@link ByteArrayOutputStream}流最终转化为什么数据实体
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/24 0:04
 */
public abstract class AbsByteArrayReceivePacket<Entity> extends ReceivePacket<ByteArrayOutputStream, Entity> {

    public AbsByteArrayReceivePacket(long len) {
        super(len);
    }

    @Override
    protected ByteArrayOutputStream createStream() {
        return new ByteArrayOutputStream((int) getLength());/*可以认为内存数据远远小于int*/
    }

}
