package com.ztiany.serbase.servlets.http;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 演示302响应
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.15 0:08
 */
public class Location302Servlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        //设置响应码
        response.setStatus(302);
        //设置响应消息头
        response.setHeader("Location", "/ServletBase/servlet/gzipServlet");
    }
}
