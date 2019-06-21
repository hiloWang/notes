package clink.box;

import java.io.ByteArrayOutputStream;

/**
 * 纯 byte 数组接收包。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/24 0:08
 */
public class BytesReceivePacket extends AbsByteArrayReceivePacket<byte[]> {

    public BytesReceivePacket(long len) {
        super(len);
    }

    @Override
    protected byte[] buildEntity(ByteArrayOutputStream stream) {
        return stream.toByteArray();
    }

    @Override
    public byte getType() {
        return TYPE_MEMORY_BYTES;
    }

}
