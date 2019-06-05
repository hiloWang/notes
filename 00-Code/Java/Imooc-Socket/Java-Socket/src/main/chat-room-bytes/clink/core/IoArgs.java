package clink.core;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * IO参数，用于执行实际的异步读写操作，读写操作状态将会以异步回调的形式通知。
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/8 22:34
 */
public class IoArgs {

    private int limit = 5;
    private byte[] byteBuffer = new byte[5];
    private ByteBuffer buffer = ByteBuffer.wrap(byteBuffer);

    /**
     * 从SocketChannel读取数据
     */
    public int readFrom(SocketChannel socketChannel) throws IOException {
        startWriting();
        int bytesProduced = 0;
        while (buffer.hasRemaining()) {
            int readLength = socketChannel.read(buffer);
            if (readLength < 0) {//无法读取到更多的数据
                throw new EOFException();
            }
            bytesProduced += readLength;
        }
        finishWriting();
        return bytesProduced;
    }

    /**
     * 写数据到SocketChannel
     */
    public int writeTo(SocketChannel socketChannel) throws IOException {
        int bytesProduced = 0;
        while (buffer.hasRemaining()) {
            int writeLength = socketChannel.write(buffer);
            if (writeLength < 0) {//无法读取到更多的数据
                throw new EOFException();
            }
            bytesProduced += writeLength;
        }
        return bytesProduced;
    }

    public int capacity() {
        return buffer.capacity();
    }

    /**
     * 设置本次读取数据的大小
     */
    public void limit(int receiveSize) {
        limit = receiveSize;
    }

    /**
     * 把数据写入到 buffer 中
     */
    public int writeTo(byte[] bytes, int offset) {
        int writeSize = Math.min(bytes.length - offset, buffer.remaining());
        buffer.get(bytes, offset, writeSize);
        return writeSize;
    }

    /**
     * 从packet中读取数据
     */
    public int readFrom(byte[] bytes, int offset) {
        int readSize = Math.min(bytes.length - offset, buffer.remaining());
        buffer.put(bytes, offset, readSize);
        return readSize;
    }

    public void writeLength(int length) {
        buffer.putInt(length);
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

    public interface IoArgsEventListener {
        void onStarted(IoArgs args);

        void onCompleted(IoArgs args);
    }


    @Override
    public String toString() {
        return "IoArgs{" +
                "limit=" + limit +
                ", buffer=" + buffer +
                '}';
    }
}
