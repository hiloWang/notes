package me.ztiany.jsonanalyze.utils;

import android.support.annotation.WorkerThread;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import okio.BufferedSource;
import okio.Okio;

public class IOUtils {

    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final String HTML = "text/html";

    private IOUtils() {
        throw new UnsupportedOperationException("no need instantiation");
    }

    @WorkerThread
    public static String convertToString(InputStream stream) {
        BufferedSource source = null;
        try {
            source = Okio.buffer(Okio.source(stream));
            return source.readUtf8();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIO(source);
        }
        return "";
    }


}
