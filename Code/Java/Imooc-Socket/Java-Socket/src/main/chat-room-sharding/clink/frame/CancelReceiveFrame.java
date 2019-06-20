package clink.frame;

import clink.core.IoArgs;

/**
 * 标识取消接受包，不需要任何操作。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/12/2 10:42
 */
public class CancelReceiveFrame extends AbsReceiveFrame {

    public CancelReceiveFrame(byte[] header) {
        super(header);
    }

    @Override
    protected int consumeBody(IoArgs args) {
        return 0;
    }

}
