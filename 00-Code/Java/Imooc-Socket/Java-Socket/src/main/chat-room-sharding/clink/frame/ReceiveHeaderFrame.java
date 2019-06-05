package clink.frame;

import clink.core.IoArgs;

public class ReceiveHeaderFrame extends AbsReceiveFrame {

    private final byte[] body;

    public ReceiveHeaderFrame(byte[] header) {
        super(header);
        body = new byte[getBodyLength()];
    }

    @Override
    protected int consumeBody(IoArgs args) {
        int offset = body.length - bodyRemaining;
        return args.writeTo(body, offset);
    }

    /**
     * 获取包的长度，一共有五个字节用于存储包的长度。
     */
    public long getPacketLength() {
        //&0xFF是为了避免字节复数符号位引起计算错误
        return ((((long) body[0]) & 0xFFL) << 32)
                | ((((long) body[1]) & 0xFFL) << 24)
                | ((((long) body[2]) & 0xFFL) << 16)
                | ((((long) body[3]) & 0xFFL) << 8)
                | (((long) body[4]) & 0xFFL);
    }

    /**
     * 获取包的类型
     */
    public byte getPacketType() {
        return body[5];
    }

    /**
     * 获取额外的头部信息
     */
    public byte[] getPacketHeaderInfo() {
        if (body.length > SendHeaderFrame.PACKET_HEADER_FRAME_MIN_LENGTH) {
            byte[] headerInfo = new byte[body.length - SendHeaderFrame.PACKET_HEADER_FRAME_MIN_LENGTH];
            System.arraycopy(body, SendHeaderFrame.PACKET_HEADER_FRAME_MIN_LENGTH,
                    headerInfo, 0, headerInfo.length);
            return headerInfo;
        }
        return null;
    }

}
