package me.ztiany.jsonanalyze.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Set;


public class JsonUtils {

    private final static Gson sGson = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .excludeFieldsWithModifiers(Modifier.STATIC)
            .create();

    public static String createJson(Map<String, ?> map) {
        JSONObject jsonObject = new JSONObject();
        Set<? extends Map.Entry<String, ?>> entries = map.entrySet();
        try {
            for (Map.Entry<String, ?> entry : entries) {
                jsonObject.put(entry.getKey(), entry.getValue());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String createJson(String[] params) {

        JSONObject jsonObject = new JSONObject();

        try {
            int length = params.length / 2;
            for (int i = 0; i < length; i++) {
                jsonObject.put(params[2 * i], params[2 * i + 1]);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }


    public static String toJson(Object entity) {
        if (entity == null) {
            return "";
        }
        try {
            return sGson.toJson(entity);
        } catch (Exception e) {
        }
        return "";
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return sGson.fromJson(json, clazz);
        } catch (Exception e) {
        }
        return null;
    }

    @SuppressWarnings("unused")
    public static <T> T fromType(String json, Type type) {
        return sGson.fromJson(json, type);
    }
}
