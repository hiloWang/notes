package com.ztiany.session.httpsession.app3;

import com.ztiany.session.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//登录界面
public class SessionApp3LoginUIServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Utils.fixEncode(request, response);

        PrintWriter out = response.getWriter();
        out.write("<form action='" + request.getContextPath() + "/servlet/SessionApp3LoginServlet' method='post'>");
        out.write("username:<input name='username'/><br/>");
        out.write("password:<input type='password' name='password'/><br/>");
        out.write("code:<input type='text' size='3' name='code'/><img src='" + request.getContextPath() + "/servlet/ImageCodeServlet'/><br/>");
        out.write("<input type='submit' value='登录'/></form>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

}
