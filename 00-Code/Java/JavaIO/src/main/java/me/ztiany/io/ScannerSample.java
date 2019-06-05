package me.ztiany.io;

import java.util.Scanner;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.23 19:12
 */
public class ScannerSample {

    public static void main(String... args) {
        //4. 发送数据给服务端
        Scanner scan = new Scanner(System.in);

        while (scan.hasNext()) {
            String str = scan.next();
            System.out.println("read---->" + str);
            if (str.equals("q")) {
                break;
            }
        }
    }
}
