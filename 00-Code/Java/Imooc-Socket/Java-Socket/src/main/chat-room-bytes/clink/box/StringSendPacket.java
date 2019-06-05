package clink.box;

import java.io.IOException;

import clink.core.SendPacket;

/**
 * 字符串包
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/18 16:50
 */
public class StringSendPacket extends SendPacket {

    private final byte[] bytes;

    public StringSendPacket(String send) {
        this.bytes = send.getBytes();
        this.length = bytes.length;
    }

    @Override
    public void close() throws IOException {
        // no op
    }

    @Override
    public byte[] bytes() {
        return bytes;
    }

}
