package clink.frame;

import clink.core.Frame;
import clink.core.IoArgs;

/**
 * 心跳发送帧
 */
public class HeartbeatSendFrame extends AbsSendFrame {

    /**
     * 心跳的固定结构
     */
    static final byte[] HEARTBEAT_DATA = new byte[]{0, 0, Frame.TYPE_COMMAND_HEARTBEAT, 0, 0, 0};

    public HeartbeatSendFrame() {
        super(HEARTBEAT_DATA);
    }

    @Override
    protected int consumeBody(IoArgs args) {
        return 0;
    }

    @Override
    public Frame nextFrame() {
        return null;
    }

}
