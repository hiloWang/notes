package com.ztiany.session.cookie.app2;

import com.ztiany.session.Constants;
import com.ztiany.session.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class CookieApp2LoginServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Utils.fixEncode(request, response);

        PrintWriter out = response.getWriter();
        //验证用户名和密码是否正确：用户名反转之后就是密码
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String remember = request.getParameter("remember");//没有勾选，返回null

        if (!username.equals(new StringBuffer(password).reverse().toString())) {
            out.write("错误的用户名或密码");
            return;
        }

        //根据用户是否勾选记住用户名：处理cookie
        Cookie c = new Cookie(Constants.LOGIN_INF, username);
        c.setPath(request.getContextPath());

        if (remember == null) {
            c.setMaxAge(0);
        } else {
            c.setMaxAge(Integer.MAX_VALUE);
        }
        response.addCookie(c);
        out.write("登录成功");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

}
