package me.ztiany.nio;

import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Map;
import java.util.Set;


/*
 * 六、字符集：Charset
 *
 *      编码：字符串 -> 字节数组
 *      解码：字节数组  -> 字符串
 */
public class CharsetSample {

    @Test
    public void defaultCharset() {
        //UTF-8
        System.out.println(Charset.defaultCharset());
    }

    @Test
    public void testAllCharset() {
        Map<String, Charset> map = Charset.availableCharsets();
        Set<Map.Entry<String, Charset>> set = map.entrySet();
        for (Map.Entry<String, Charset> entry : set) {
            System.out.println(entry.getKey() + "=" + entry.getValue());
        }
    }


    //字符集
    @Test
    public void testEncodeDecode() throws IOException {
        Charset csGBK = Charset.forName("GBK");

        //获取编码器
        CharsetEncoder charsetEncoder = csGBK.newEncoder();
        //获取解码器
        CharsetDecoder charsetDecoder = csGBK.newDecoder();

        CharBuffer charBuffer = CharBuffer.allocate(1024);
        charBuffer.put("我爱编程，我很快乐！");
        charBuffer.flip();

        //编码
        ByteBuffer byteBuffer = charsetEncoder.encode(charBuffer);
        System.out.println(byteBuffer);

        int remaining = byteBuffer.remaining();
        for (int i = 0; i < remaining; i++) {
            System.out.println(byteBuffer.get());
        }
        System.out.println(byteBuffer);

        //解码
        byteBuffer.flip();
        System.out.println(byteBuffer);

        CharBuffer cBuf2 = charsetDecoder.decode(byteBuffer);
        System.out.println(cBuf2.toString());

        System.out.println("------------------------------------------------------");
        Charset charset = Charset.forName("GBK");
        byteBuffer.flip();
        CharBuffer cBuf3 = charset.decode(byteBuffer);
        System.out.println(cBuf3.toString());
    }

}
