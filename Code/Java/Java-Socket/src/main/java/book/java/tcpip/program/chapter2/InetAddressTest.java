package book.java.tcpip.program.chapter2;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.24 15:14
 */
public class InetAddressTest {

    public static void main(String... args){
        try {
            InetAddress inetAddress = InetAddress.getByName("localhost");
            System.out.println(inetAddress);
            //isReachable是否可以发生数据交换
            System.out.println(inetAddress.isReachable(5000));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
