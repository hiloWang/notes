package com.ztiany.jspbase.servlet;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.1 15:10
 */
public class I18NServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/htm;charset=utf-8");

        //读默认的资源消息包：根据地区
        ResourceBundle rb = ResourceBundle.getBundle("com.ztiany.jspbase.resources.msg");
        String hello = rb.getString("hello");
        response.getWriter().write(hello);

        //读取指定区域的资源包
        ResourceBundle usRB = ResourceBundle.getBundle("com.ztiany.jspbase.resources.msg", Locale.US);
        String usHello = usRB.getString("hello");
        response.getWriter().write(usHello);

    }

}
