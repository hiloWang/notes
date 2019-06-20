package me.ztiany.okio.official;

import okio.Buffer;
import okio.ByteString;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * 测试标准：确认用当前程序可以安全地解码用早期版本的程序编码的数据。
 */
public final class GoldenValue {

    public void run() throws Exception {

        Point point = new Point(8.0, 15.0);
        ByteString pointBytes = serialize(point);
        System.out.println(pointBytes.base64());

        ByteString goldenBytes = ByteString.decodeBase64("rO0ABXNyACltZS56dGlhbnkub2tpby5vZmZpY2lhbC5Hb2xkZW5WYWx1ZSRQb2ludP5dqpqh2r" +
                "U1AgACRAABeEQAAXl4cEAgAAAAAAAAQC4AAAAAAAA=");

        Point decoded = (Point) deserialize(goldenBytes);
        assertEquals(point, decoded);
    }

    private ByteString serialize(Object o) throws IOException {
        Buffer buffer = new Buffer();
        try (ObjectOutputStream objectOut = new ObjectOutputStream(buffer.outputStream())) {
            objectOut.writeObject(o);
        }
        return buffer.readByteString();
    }

    private Object deserialize(ByteString byteString) throws IOException, ClassNotFoundException {
        Buffer buffer = new Buffer();
        buffer.write(byteString);
        try (ObjectInputStream objectIn = new ObjectInputStream(buffer.inputStream())) {
            Object result = objectIn.readObject();
            if (objectIn.read() != -1) throw new IOException("Unconsumed bytes in stream");
            return result;
        }
    }

    static final class Point implements Serializable {

        double x;
        double y;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }
    }

    private void assertEquals(Point a, Point b) {
        if (a.x != b.x || a.y != b.y) throw new AssertionError();
    }

    public static void main(String... args) throws Exception {
        new GoldenValue().run();
    }
} 