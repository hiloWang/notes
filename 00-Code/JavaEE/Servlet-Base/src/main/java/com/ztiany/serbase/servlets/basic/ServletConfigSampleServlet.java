package com.ztiany.serbase.servlets.basic;

import java.util.Enumeration;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 使用ServletConfig获取配置参数
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.15 0:24
 */
public class ServletConfigSampleServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        Enumeration e = getServletConfig().getInitParameterNames();// 参数的名称
        while (e.hasMoreElements()) {
            String paramName = (String) e.nextElement();
            System.out.println(paramName + "=" + getServletConfig().getInitParameter(paramName));
        }
    }
}
