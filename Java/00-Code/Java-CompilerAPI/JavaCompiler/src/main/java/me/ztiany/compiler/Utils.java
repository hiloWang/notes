package me.ztiany.compiler;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2019/4/10 15:01
 */
public class Utils {

    public static void close(Closeable closeable) {
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
