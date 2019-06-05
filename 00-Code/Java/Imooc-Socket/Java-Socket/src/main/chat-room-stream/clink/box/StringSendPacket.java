package clink.box;

/**
 * 字符串包
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/18 16:50
 */
public class StringSendPacket extends BytesSendPacket {

    /**
     * 字符串发送时就是Byte 数组，所以直接得到 Byte 数组，并按照Byte的发送方式发送即可
     *
     * @param send 字符串
     */
    public StringSendPacket(String send) {
        super(send.getBytes());
    }

    @Override
    public byte getType() {
        return TYPE_MEMORY_STRING;
    }

}
