package me.ztiany.nio;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.23 18:46
 */
class Utils {

    static void close(Closeable fileInputStream) {
        if (fileInputStream != null) {
            try {
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
