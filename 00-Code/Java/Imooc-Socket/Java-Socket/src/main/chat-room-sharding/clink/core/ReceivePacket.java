package clink.core;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 接收包的定义，不同的数据类型对应不同的 ReceivePack 实现。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/18 16:38
 */
public abstract class ReceivePacket<Stream extends OutputStream, Entity> extends Packet<Stream> {

    // 定义当前接收包最终的实体
    private Entity mEntity;

    public ReceivePacket(long len) {
        this.mLength = len;
    }

    /**
     * 得到最终接收到的实体
     *
     * @return 实体类型
     */
    public Entity getEntity() {
        return mEntity;
    }

    /**
     * 根据接收到的流转化为对应的实体
     *
     * @param stream {@link OutputStream}
     * @return 实体
     */
    protected abstract Entity buildEntity(Stream stream);

    /**
     * 先关闭流，随后将流的内容转化为对应的实体
     *
     * @param stream 待关闭的流
     * @throws IOException IO异常
     */
    @Override
    protected final void closeStream(Stream stream) throws IOException {
        super.closeStream(stream);
        mEntity = buildEntity(stream);
    }

}
