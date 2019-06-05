package clink.core;

/**
 * 发送包的定义
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/18 17:18
 */
public abstract class SendPacket extends Packet {

    private boolean isCanceled = false;

    public abstract byte[] bytes();

    public boolean isCanceled() {
        return isCanceled;
    }

}
