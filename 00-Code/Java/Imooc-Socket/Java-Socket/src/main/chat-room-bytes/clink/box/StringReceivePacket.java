package clink.box;

import java.io.IOException;

import clink.core.ReceivePacket;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/18 16:52
 */
public class StringReceivePacket extends ReceivePacket {

    /*临时容器*/
    private byte[] buffer;
    /*已经读取到的位置*/
    private int position;

    /**
     * @param bufferLength 存储这个字符串需要的字节长度
     */
    public StringReceivePacket(int bufferLength) {
        buffer = new byte[bufferLength];
        position = 0;
        this.length = bufferLength;
    }

    @Override
    public void save(byte[] bytes, int count) {
        System.arraycopy(bytes, 0, buffer, position, count);
        position += count;
    }

    @Override
    public String toString() {
        return new String(buffer);
    }

    @Override
    public void close() throws IOException {
        //no op
    }

}
