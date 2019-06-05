package com.ztiany.register.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommonUtil {

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 把请求参数值封装到JavaBean中
     */
    public static <T> T fillBeanFromRequest(HttpServletRequest request, Class<T> clazz) {
        try {
            T bean = clazz.newInstance();
            BeanUtils.populate(bean, request.getParameterMap());
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public static void setNoCache(HttpServletResponse response) {
        //通知浏览器不要缓存
        response.setHeader("Expires", "-1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
    }

    public static void setUTF8Encode(HttpServletRequest request, HttpServletResponse response) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.setContentType("text/html;charset=utf-8");

    }

    static {
        ConvertUtils.register((type, value) -> {
            if (value instanceof String && type == Date.class) {
                //String--->java.util.Date
                try {
                    return parse((String) value);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }
            return null;
        }, Date.class);

        //ConvertUtils.register(new DateLocaleConverter(), Date.class);
    }

    public static void copyProperties(Object dest, Object source) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.copyProperties(dest, source);
    }

    public static String formatDate(Date date) {
        return DATE_FORMAT.format(date);
    }

    public static Date parse(String sDate) throws ParseException {
        return DATE_FORMAT.parse(sDate);
    }
}
