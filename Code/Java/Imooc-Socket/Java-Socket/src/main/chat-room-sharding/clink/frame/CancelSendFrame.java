package clink.frame;

import clink.core.Frame;
import clink.core.IoArgs;

public class CancelSendFrame extends AbsSendFrame {

    public CancelSendFrame(short identifier) {
        super(
                0,
                Frame.TYPE_COMMAND_SEND_CANCEL,
                Frame.FLAG_NONE,
                identifier);
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
