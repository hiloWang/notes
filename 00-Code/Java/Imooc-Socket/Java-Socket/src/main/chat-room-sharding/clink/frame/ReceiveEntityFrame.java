package clink.frame;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

import clink.core.IoArgs;

/**
 * 取消对应的packet发送
 */
public class ReceiveEntityFrame extends AbsReceiveFrame {

    private WritableByteChannel channel;

    ReceiveEntityFrame(byte[] header) {
        super(header);
    }

    public void bindPacketChannel(WritableByteChannel channel) {
        this.channel = channel;
    }

    @Override
    protected int consumeBody(IoArgs args) throws IOException {
        return channel == null ? args.setEmpty(bodyRemaining) : args.writeTo(channel);
    }

}
