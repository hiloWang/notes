package com.ztiany.mall.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.17 22:14
 */
public class WebBeanUtils {

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static String formatDate(Date date) {
        return FORMAT.format(date);
    }

    static {
        //自己指定一个类型转换器（将String转成Date）
        ConvertUtils.register((clazz, value) -> {
            //将string转成date
            Date parse = null;
            try {
                parse = FORMAT.parse(value.toString());
            } catch (ParseException e) {
                e.printStackTrace();
                LogUtils.warn("WebBeanUtils ConvertUtils convert error", e);
            }
            return parse;
        }, Date.class);
    }

    public static void populate(Object bean, Map properties) throws InvocationTargetException, IllegalAccessException {
        BeanUtils.populate(bean, properties);
    }

    public static int parsePage(String pageNum) {
        int currentPage = 1;
        if (pageNum != null && !pageNum.equals("")) {
            try {
                currentPage = Integer.parseInt(pageNum);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return currentPage;
    }

}
