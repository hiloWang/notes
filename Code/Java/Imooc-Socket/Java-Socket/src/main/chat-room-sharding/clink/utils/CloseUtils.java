package clink.utils;

import java.io.Closeable;
import java.io.IOException;

public class CloseUtils {

    public static void close(Closeable... closeables) {
        if (closeables == null) {
            return;
        }
        for (Closeable closeable : closeables) {
            try {
                if (closeable == null) {
                    continue;
                }
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
