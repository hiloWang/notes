package com.ztiany.serbase.servlets.request;

import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.16 0:24
 */
public class RequestPropertiesServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        getBaseProperties(request);
        getRequestProperties(request);
    }

    @SuppressWarnings("unused")
    private void getRequestProperties(HttpServletRequest request) {
        //获取单一请求消息头
        String value = request.getHeader("Accept-Encoding");

        //获取重名的消息头值
        Enumeration encodeE = request.getHeaders("Accept-Encoding");//得到的是值集
        while (encodeE.hasMoreElements()) {
            String v = (String) encodeE.nextElement();
            System.out.println(v);
        }

        //获取所有的请求消息头名称和取值
        Enumeration allE = request.getHeaderNames();//头名称
        while (allE.hasMoreElements()) {
            String headerName = (String) allE.nextElement();
            System.out.println(headerName + ":" + request.getHeader(headerName));
        }

    }

    private void getBaseProperties(HttpServletRequest request) {
        String protocol = request.getProtocol();//客户端使用的协议
        String method = request.getMethod();//客户端的请求方式
        String uri = request.getRequestURI();// /ServletBase/servlet/RequestPropertiesServlet
        String url = request.getRequestURL().toString();//   http://localhost:8080/ServletBase/servlet/RequestPropertiesServlet
        String remoteAddress = request.getRemoteAddr();//来访者的ip
        int remotePort = request.getRemotePort();//来访者机器用的端口号(随机)
        String queryString = request.getQueryString();//查询字符串

        System.out.println(protocol);
        System.out.println(method);
        System.out.println(uri);
        System.out.println(url);
        System.out.println(remoteAddress);
        System.out.println(remotePort);
        System.out.println(queryString);

        String contextPath = request.getContextPath();//上下文路径：/ServletBase
        System.out.println(contextPath);
    }
}
