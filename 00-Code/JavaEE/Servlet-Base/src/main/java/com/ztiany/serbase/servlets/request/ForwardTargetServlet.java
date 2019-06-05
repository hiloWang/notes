package com.ztiany.serbase.servlets.request;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * * 请求转发：目标
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.16 0:13
 */
public class ForwardTargetServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        System.out.println("ForwardTargetServlet：" + request);
        System.out.println("keyA：" + request.getAttribute("keyA"));

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("Forward成功，哈哈哈");
    }
}
