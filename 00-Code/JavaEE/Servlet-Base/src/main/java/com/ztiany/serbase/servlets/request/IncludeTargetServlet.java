package com.ztiany.serbase.servlets.request;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 包含：目标
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.16 0:14
 */
public class IncludeTargetServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println("IncludeTargetServlet：" + response);
        //在目标组件中对响应状态代码或者响应头所做的修改都会被忽略。
        //response.setContentType("text/html;charset=UTF-8");//no need
        response.getWriter().write("抖音熊");
    }
}
