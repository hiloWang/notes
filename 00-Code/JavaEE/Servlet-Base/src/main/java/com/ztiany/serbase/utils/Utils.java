package com.ztiany.serbase.utils;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.15 1:02
 */
public class Utils {

    private Utils() {
    }

    public static void setNoCache(HttpServletResponse response) {
        //通知浏览器不要缓存
        response.setHeader("Expires", "-1");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Pragma", "no-cache");
    }

}
