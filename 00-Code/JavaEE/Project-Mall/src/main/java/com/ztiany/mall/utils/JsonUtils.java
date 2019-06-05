package com.ztiany.mall.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Modifier;
import java.lang.reflect.Type;


public class JsonUtils {

    private final static Gson sGson = new GsonBuilder()
            .excludeFieldsWithModifiers(Modifier.TRANSIENT)
            .excludeFieldsWithModifiers(Modifier.STATIC)
            .create();

    public static String toJson(Object entity) {
        if (entity == null) {
            return "";
        }
        try {
            return sGson.toJson(entity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return sGson.fromJson(json, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unused")
    public static <T> T fromType(String json, Type type) {
        return sGson.fromJson(json, type);
    }
}
