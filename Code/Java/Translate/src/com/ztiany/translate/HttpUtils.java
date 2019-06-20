package com.ztiany.translate;


import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Ztiany
 *         Email ztiany3@gmail.com
 *         Date 17.10.15 15:53
 */
class HttpUtils {

    private static final Gson GSON = new Gson();

    static void doGet(String address, Callback callback) {
        //不要开启子线程
        request(address, callback);
    }

    private static void request(String address, Callback callback) {
        try {
            URL url = new URL(address);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            int responseCode = connection.getResponseCode();
            if (responseCode != 200) {
                callback.onError(new Exception(""));
                return;
            }
            InputStream inputStream = connection.getInputStream();
            byte[] buff = new byte[1024];
            int len;
            StringBuilder sb = new StringBuilder();
            while ((len = inputStream.read(buff)) != -1) {
                sb.append(new String(buff, 0, len));
            }
            callback.onResult(GSON.fromJson(sb.toString(), Result.class));
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError(e);
        }
    }

    public interface Callback {

        void onResult(Result string);

        void onError(Exception e);
    }

}
