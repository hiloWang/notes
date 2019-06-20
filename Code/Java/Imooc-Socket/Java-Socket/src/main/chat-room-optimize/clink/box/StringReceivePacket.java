package clink.box;

import java.io.ByteArrayOutputStream;

/**
 * 字符串接收包
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/18 16:52
 */
public class StringReceivePacket extends AbsByteArrayReceivePacket<String> {

    /**
     * @param bufferLength 存储这个字符串需要的字节长度
     */
    public StringReceivePacket(long bufferLength) {
        super(bufferLength);
    }

    @Override
    public byte getType() {
        return TYPE_MEMORY_STRING;
    }

    @Override
    protected String buildEntity(ByteArrayOutputStream stream) {
        return new String(stream.toByteArray());
    }

}
