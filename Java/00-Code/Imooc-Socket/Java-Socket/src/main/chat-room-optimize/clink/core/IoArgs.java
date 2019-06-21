package clink.core;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

/**
 * IO参数，用于执行实际的异步读写操作，读写操作状态将会以异步回调的形式通知。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/8 22:34
 */
@SuppressWarnings("Duplicates")
public class IoArgs {

    private int limit = 256;
    private byte[] byteBuffer = new byte[256];
    private ByteBuffer buffer = ByteBuffer.wrap(byteBuffer);

    /**
     * 从bytes数组进行消费
     */
    public int readFrom(byte[] bytes, int offset, int count) {
        int size = Math.min(count, buffer.remaining());
        if (size <= 0) {
            return 0;
        }
        buffer.put(bytes, offset, size);
        return size;
    }

    /**
     * 写入数据到bytes中
     */
    public int writeTo(byte[] bytes, int offset) {
        int size = Math.min(bytes.length - offset, buffer.remaining());
        buffer.get(bytes, offset, size);
        return size;
    }

    /**
     * 从SocketChannel读取数据
     */
    public int readFrom(SocketChannel socketChannel) throws IOException {
        ByteBuffer localBuffer = this.buffer;
        int bytesProduced = 0;
        int len;
        // 读取或写数据到Socket原理
        // 回调当前可读、可写时我们进行数据填充或者消费
        // 但是过程中可能SocketChannel资源被其他SocketChannel占用了资源（网卡把资源让给了另外一个SocketChannel）
        // 那么我们应该让出当前的线程调度，让应该得到数据消费的SocketChannel的到CPU调度
        // 而不应该单纯的buffer.hasRemaining()判断
        do {
            len = socketChannel.read(localBuffer);
            if (len < 0) {//无法读取到更多的数据
                //Selector 选择后却又读不到数据，说明连接出问题了
                throw new EOFException("Cannot read any data with:" + socketChannel);
            }
            bytesProduced += len;
        } while (localBuffer.hasRemaining() && len != 0);

        return bytesProduced;
    }

    /**
     * 写数据到SocketChannel
     */
    public int writeTo(SocketChannel socketChannel) throws IOException {
        int bytesProduced = 0;
        ByteBuffer localBuffer = this.buffer;
        int writeLength;

        do {
            writeLength = socketChannel.write(localBuffer);
            if (writeLength < 0) {//无法读取到更多的数据
                //Selector 选择后却又写不出数据，说明连接出问题了
                throw new EOFException("Current write any data with:" + socketChannel);
            }
            bytesProduced += writeLength;
        } while (buffer.hasRemaining());

        return bytesProduced;
    }

    public int capacity() {
        return buffer.capacity();
    }

    /**
     * 设置本次读取数据的大小
     */
    public void limit(int receiveSize) {
        limit = Math.min(receiveSize, buffer.capacity());
    }

    /**
     * 把数据写入到 writableByteChannel 中
     */
    public int writeTo(WritableByteChannel writableByteChannel) throws IOException {
        int bytesProduced = 0;
        while (buffer.hasRemaining()) {
            int writeLength = writableByteChannel.write(buffer);
            if (writeLength < 0) {//无法读取到更多的数据
                throw new EOFException();
            }
            bytesProduced += writeLength;
        }
        return bytesProduced;
    }

    /**
     * 从 readableByteChannel 中读取数据
     */
    public int readFrom(ReadableByteChannel readableByteChannel) throws IOException {
        int bytesProduced = 0;
        while (buffer.hasRemaining()) {
            int writeLength = readableByteChannel.read(buffer);
            if (writeLength < 0) {//无法读取到更多的数据
                throw new EOFException();
            }
            bytesProduced += writeLength;
        }
        return bytesProduced;
    }

    public int readLength() {
        return buffer.getInt();
    }

    public void startWriting() {
        //清理，开始写入数据
        buffer.clear();
        // 定义容纳区间
        buffer.limit(limit);
    }

    public void finishWriting() {
        //切换到读取模式
        buffer.flip();
    }

    public boolean remained() {
        return buffer.remaining() > 0;
    }

    /**
     * 填充数据
     *
     * @param size 想要填充数据的长度
     * @return 真实填充数据的长度
     */
    public int fillEmpty(int size) {
        int fillSize = Math.min(size, buffer.remaining());
        //直接修改position
        buffer.position(buffer.position() + fillSize);
        return fillSize;
    }

    /**
     * 填充空数据
     */
    public int setEmpty(int size) {
        int fillSize = Math.min(size, buffer.remaining());
        //直接修改position
        buffer.position(buffer.position() + fillSize);
        return fillSize;
    }

    /**
     * IoArgs 提供者、处理者；数据的生产或消费者。定义为这种形式，用于异步处理IO。
     */
    public interface IoArgsEventProcessor {
        /**
         * 提供一份可消费的IoArgs
         */
        IoArgs provideIoArgs();

        /**
         * 消费失败时回调
         *
         * @param ioArgs IoArgs
         * @param e      异常信息
         */
        void onConsumeFailed(IoArgs ioArgs, Exception e);


        /**
         * 消费成功
         *
         * @param args IoArgs
         */
        void onConsumeCompleted(IoArgs args);
    }


    @Override
    public String toString() {
        return "IoArgs{" +
                "limit=" + limit +
                ", buffer=" + buffer +
                '}';
    }
}
