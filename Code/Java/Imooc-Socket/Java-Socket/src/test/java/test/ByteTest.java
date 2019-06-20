package test;

import org.junit.Test;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/25 20:14
 */
public class ByteTest {

    @Test
    public void testByte() {
        /*
        11111111111111111111111111111111
        11111111111111111111111111111111
        11111111111111111111111111111111
        11111111111111111111111111111111
         */
        byte b = -1;
        int i = b;
        System.out.println(Integer.toBinaryString(i));
        System.out.println(Integer.toBinaryString(i >> 8));
        System.out.println(Integer.toBinaryString(i >> 8));
        System.out.println(Integer.toBinaryString(i >> 8));
        int c = 128;
        byte a = (byte) c;
        System.out.println(a);
    }

}
