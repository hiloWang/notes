package com.ztiany.serbase.servlets.request;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * * 包含：源
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.16 0:14
 */
public class IncludeSourceServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("IncludeSourceServlet：" + response);
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("我喜欢");
        RequestDispatcher rd = request.getRequestDispatcher("/servlet/IncludeTargetServlet");
        rd.include(request, response);
    }

}
