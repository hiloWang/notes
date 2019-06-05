package com.ztiany.serbase.servlets.basic;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 通过ServletContext转发请求到其他Servlet
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.15 0:30
 */
public class ServletContextSample2Servlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletContext sc = getServletContext();
        //路径：转给谁。地址，地址必须以/开头。这里的路径相对与/ServletBase
        //理解1："/servlet/ServletContextDemo7"的前面会自动加上/ServletBase
        //理解2：这里已经作内部处理，都是相对于/ServletBase的
        RequestDispatcher rd = sc.getRequestDispatcher("/servlet/gzipServlet");
        rd.forward(request, response);
    }
}
