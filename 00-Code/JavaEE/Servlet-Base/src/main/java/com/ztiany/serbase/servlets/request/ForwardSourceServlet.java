package com.ztiany.serbase.servlets.request;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 请求转发：源
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.16 0:13
 */
public class ForwardSourceServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    /*
     * ServletContext.getRequestDispatcher(String uri);
     * ServletRequest.getRequestDispatcher(String uri);
     *
     * 区别在于参数uri的写法上面：
     *  ServletContext.getRequestDispatcher(String uri);URI地址必须以"/"开头，表示当前应用“/ServletBase”（绝对路径）
     *
     *  ServletRequest.getRequestDispatcher(String uri);    URI如果以"/"开头，表示当前应用“/ServletBase”（绝对路径）
     *                                                                               URI如果不以“/”开头，表示相对路径
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("ForwardSourceServlet：" + request);

        request.setAttribute("keyA", "valueA");

        //方式一：利用ServletContext获取RequestDispatcher
        //ServletContext sc = getServletContext();
        //RequestDispatcher rd = sc.getRequestDispatcher("/servlet/ForwardSourceServlet");
        //rd.forward(request, response);//请求转发

        //方式二：利用ServletRequest获取RequestDispatcher，此时相对于“/ServletBase/ForwardSourceServlet”
        RequestDispatcher rd = request.getRequestDispatcher("ForwardTargetServlet");
        rd.forward(request, response);//请求转发
    }
}
