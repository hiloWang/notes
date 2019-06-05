package me.ztiany.socket.longlive;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/10/27 16:32
 */
class Utils {

    static void close(Closeable bufferedInputStream) {
        if (bufferedInputStream != null) {
            try {
                bufferedInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
