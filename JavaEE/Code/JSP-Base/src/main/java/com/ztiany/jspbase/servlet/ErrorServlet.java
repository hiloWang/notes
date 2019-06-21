package com.ztiany.jspbase.servlet;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ErrorServlet发生错误会统一转到web.xml中配置的错误界面
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.21 0:41
 */
public class ErrorServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        int a = 10 / 0;
    }


}
