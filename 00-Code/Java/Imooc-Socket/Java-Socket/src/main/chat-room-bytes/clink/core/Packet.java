package clink.core;

import java.io.Closeable;

/**
 * 公共的数据封装，提供了类型以及数据长度的定义。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/18 16:35
 */
public abstract class Packet implements Closeable {

    /**
     * 包的长度
     */
    protected int length;

    /**
     * 包所代表的数据类型
     */
    protected byte type;

    public byte getType() {
        return type;
    }

    public int getLength() {
        return length;
    }

}
