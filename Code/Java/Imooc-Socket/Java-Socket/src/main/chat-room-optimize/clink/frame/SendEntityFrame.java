package clink.frame;

import java.io.IOException;
import java.nio.channels.ReadableByteChannel;

import clink.core.Frame;
import clink.core.IoArgs;
import clink.core.SendPacket;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/27 0:02
 */
public class SendEntityFrame extends AbsSendPacketFrame {

    private final ReadableByteChannel mChannel;
    private final long mUnConsumeEntityLength;


    SendEntityFrame(short identifier, long entityLength, ReadableByteChannel channel, SendPacket<?> packet) {
        super(
                (int) Math.min(entityLength, Frame.MAX_CAPACITY)/*一个帧的大小肯定是有规定的，不大于65535*/,
                Frame.TYPE_PACKET_ENTITY,
                Frame.FLAG_NONE,
                identifier,
                packet);

        /*还未消费的实体长度 = 剩余实体的总长度 - 当前包的body长度*/
        mUnConsumeEntityLength = entityLength - mBodyRemaining;
        this.mChannel = channel;
    }

    @Override
    protected Frame buildNextFrame() {
        //mUnConsumeEntityLength == 0 表示该实体已经消费完了
        if (mUnConsumeEntityLength == 0) {
            return null;
        }
        // 将未消费的长度用于构建下一帧
        return new SendEntityFrame(getBodyIdentifier(), mUnConsumeEntityLength, mChannel, mPacket);
    }

    @Override
    protected int consumeBody(IoArgs args) throws IOException {
        if (mPacket == null) {
            // 已终止当前帧，则填充假数据。
            //对于被 abort 的帧处理有两种方式：
            //1 把未发送的数据读取到内存中，之后从内存中读取数据发送（可靠性更高，内存消耗较大）
            //2 填充假数据发送，通过一定机制，告诉客户端哪一段是假数据，可以丢弃。
            return args.fillEmpty(mBodyRemaining);
        }
        return args.readFrom(mChannel);
    }

}
