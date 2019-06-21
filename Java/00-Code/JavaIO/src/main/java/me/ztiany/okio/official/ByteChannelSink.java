package me.ztiany.okio.official;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import okio.Buffer;
import okio.Sink;
import okio.Timeout;

/**
 * Creates a Sink around a WritableByteChannel and efficiently writes data using an UnsafeCursor.
 * <p>
 * 创建一个基于可写入的字节通道的 Sink，有效地使用不安全的光标写数据。
 *
 * <p>This is a basic example showing another use for the UnsafeCursor. Using the
 * {@link ByteBuffer#wrap(byte[], int, int) ByteBuffer.wrap()} along with access to Buffer segments,
 * a WritableByteChannel can be given direct access to Buffer data without having to copy the data.
 * 一个可写的字节通道可直接访问缓冲区数据而不需要复制数据。
 */
final class ByteChannelSink implements Sink {

    private final WritableByteChannel channel;
    private final Timeout timeout;

    private final Buffer.UnsafeCursor cursor = new Buffer.UnsafeCursor();

    ByteChannelSink(WritableByteChannel channel, Timeout timeout) {
        this.channel = channel;
        this.timeout = timeout;
    }

    @Override
    public void write(Buffer source, long byteCount) throws IOException {
        if (!channel.isOpen()) throw new IllegalStateException("closed");
        if (byteCount == 0) return;

        long remaining = byteCount;
        while (remaining > 0) {
            timeout.throwIfReached();

            try (Buffer.UnsafeCursor ignored = source.readUnsafe(cursor)) {
                cursor.seek(0);
                int length = (int) Math.min(cursor.end - cursor.start, remaining);
                int written = channel.write(ByteBuffer.wrap(cursor.data, cursor.start, length));
                remaining -= written;
                source.skip(written);
            }
        }
    }

    @Override
    public void flush() {
    }

    @Override
    public Timeout timeout() {
        return timeout;
    }

    @Override
    public void close() throws IOException {
        channel.close();
    }
} 