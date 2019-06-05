package clink.core;

import java.io.IOException;

/**
 * 帧-用于包的分片。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/25 23:08
 */
public abstract class Frame {

    /*
    帧的结构：
        当前帧的大小：两个字节
        帧类型：一个字节
        帧标志信息：一个字节，扩展用
        对应包唯一标识：一个字节
        预留空间：一个字节
        数据区：可用空间为出去以上数据的剩余空间
     */

    // 帧头长度
    public static final int FRAME_HEADER_LENGTH = 6;
    // 单帧最大容量 64KB，帧最接近底层的数据，每一帧的数据量也较小。一个包由一个头帧和若干个实体帧组成。
    public static final int MAX_CAPACITY = 64 * 1024 - 1;//2^16 -1

    // Packet头信息帧
    public static final byte TYPE_PACKET_HEADER = 11;
    // Packet数据分片信息帧
    public static final byte TYPE_PACKET_ENTITY = 12;

    // 指令-发送取消
    public static final byte TYPE_COMMAND_SEND_CANCEL = 41;
    // 指令-接受拒绝
    public static final byte TYPE_COMMAND_RECEIVE_REJECT = 42;
    //心跳
    public static final byte TYPE_COMMAND_HEARTBEAT = 81;

    // Flag标记
    public static final byte FLAG_NONE = 0;

    // 头部6字节固定
    protected final byte[] header = new byte[FRAME_HEADER_LENGTH];

    /**
     * @param length     帧的长度
     * @param type       帧的类型
     * @param flag       用于扩展
     * @param identifier 一个包的多个帧具有相同的 identifier
     */
    public Frame(int length, byte type, byte flag, short identifier) {
        if (length < 0 || length > MAX_CAPACITY) {
            throw new RuntimeException("The Body length of a single frame should be between 0 and " + MAX_CAPACITY);
        }

        if (identifier < 1 || identifier > 255) {
            throw new RuntimeException("The Body identifier of a single frame should be between 1 and 255");
        }

        // 00000000 00000000 00000000 01000000
        header[0] = (byte) (length >> 8);//长度高位
        header[1] = (byte) (length);//长度低位

        header[2] = type;//类型
        header[3] = flag;

        header[4] = (byte) identifier;//标记
        header[5] = 0;//预留空间
    }

    public Frame(byte[] header) {
        System.arraycopy(header, 0, this.header, 0, FRAME_HEADER_LENGTH);
    }

    /**
     * 获取Body的长度
     *
     * @return 当前帧Body总长度[0~MAX_CAPACITY]
     */
    public int getBodyLength() {
        //& 0xFF 的原因，复数是采用反码存储的。
        //如果存在负数，则 (int)byte 会导致高位全部转换为 1111。比如
        //byte 类型的 -1 强转为 int，二进制为 11111111 11111111 11111111 11111111，因此要去掉高位的 1 则应该 &FF。
        return (((int) header[0] & 0xFF) << 8) | (((int) header[1] & 0xFF));
    }

    /**
     * 获取Body的类型
     *
     * @return 类型[0~255]
     */
    public byte getBodyType() {
        return header[2];
    }

    /**
     * 获取Body的Flag
     *
     * @return Flag
     */
    public byte getBodyFlag() {
        return header[3];
    }

    /**
     * 获取Body的唯一标志
     *
     * @return 标志[0~255]
     */
    public short getBodyIdentifier() {
        return (short) (((short) header[4]) & 0xFF);
    }

    /**
     * 进行数据读或写操作
     *
     * @param args 数据
     * @return 是否已消费完全， True：则无需再传递数据到Frame或从当前Frame读取数据
     */
    public abstract boolean handle(IoArgs args) throws IOException;

    /**
     * 基于当前帧尝试构建下一份待消费的帧，只有在一帧消费完后才会创建接下来的帧，有利于减少内存消耗。
     *
     * @return Frame 待消费的帧。
     */
    public abstract Frame nextFrame();

    /**
     * 获取可以消费数据的长度
     *
     * @return 长度
     */
    public abstract int getConsumableLength();

}
