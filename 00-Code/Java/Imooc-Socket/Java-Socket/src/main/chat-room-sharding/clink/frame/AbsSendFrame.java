package clink.frame;

import java.io.IOException;

import clink.core.Frame;
import clink.core.IoArgs;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/26 23:17
 */
public abstract class AbsSendFrame extends Frame {

    //多个线程并发地发送帧，需要保证可见性
    // 帧头可读写区域大小
    volatile byte mHeaderRemaining = Frame.FRAME_HEADER_LENGTH;
    // 帧体可读写区域大小
    volatile int mBodyRemaining;

    public AbsSendFrame(int length, byte type, byte flag, short identifier) {
        super(length, type, flag, identifier);
        mBodyRemaining = length;
    }

    @Override
    public synchronized boolean handle(IoArgs args) throws IOException {
        try {
            args.limit(mHeaderRemaining + mBodyRemaining);
            args.startWriting();
            if (mHeaderRemaining > 0 && args.remained()) {
                mHeaderRemaining -= consumeHeader(args);
            }
            if (mHeaderRemaining == 0 && mBodyRemaining > 0 && args.remained()) {
                mBodyRemaining -= consumeBody(args);
            }
            return mHeaderRemaining == 0 && mBodyRemaining == 0;
        } finally {
            args.finishWriting();
        }
    }

    protected abstract int consumeBody(IoArgs args) throws IOException;

    private byte consumeHeader(IoArgs args) {
        int count = mHeaderRemaining;//还要消费多少
        int offset = header.length - count;//已经消费了多少
        return (byte) args.readFrom(header, offset, count);
    }

    /**
     * 是否已经处于发送数据中，如果已经发送了部分数据则返回True，
     * 只要头部数据已经开始消费，则肯定已经处于发送数据中
     *
     * @return True，已发送部分数据
     */
    protected synchronized boolean isSending() {
        return mHeaderRemaining < Frame.FRAME_HEADER_LENGTH;
    }

    @Override
    public int getConsumableLength() {
        return mHeaderRemaining + mBodyRemaining;
    }

}
