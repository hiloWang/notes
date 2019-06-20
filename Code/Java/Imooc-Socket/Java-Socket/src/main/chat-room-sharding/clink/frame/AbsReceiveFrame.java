package clink.frame;

import java.io.IOException;

import clink.core.Frame;
import clink.core.IoArgs;

/**
 * 接收帧的基础实现，对于接收帧而言，头部信息肯定是在外层解析的，根据解析到的头部信息来构建不同类型的帧。
 */
public abstract class AbsReceiveFrame extends Frame {

    /*还有多少没有接收*/
    volatile int bodyRemaining;

    public AbsReceiveFrame(byte[] header) {
        super(header);
        bodyRemaining = getBodyLength();
    }

    @Override
    public synchronized boolean handle(IoArgs args) throws IOException {
        if (bodyRemaining == 0) {
            return true;
        }
        bodyRemaining -= consumeBody(args);
        //当bodyRemaining为0时，则表示全部消费完毕
        return bodyRemaining == 0;
    }

    protected abstract int consumeBody(IoArgs args) throws IOException;

    //不存在下一帧的概念
    @Override
    public Frame nextFrame() {
        return null;
    }

    @Override
    public int getConsumableLength() {
        return bodyRemaining;
    }

}
