package com.ztiany.serbase.servlets.http;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 演示设置不要缓存
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.14 23:56
 */
public class NoCacheServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setHeader("Expires", "-1");
        response.setHeader("Cache-Control", "no-cache");//HTTP1.1
        response.setHeader("Pragma", "no-cache");// HTTP1.0
        response.getWriter().write("Hello IE");
    }
}
