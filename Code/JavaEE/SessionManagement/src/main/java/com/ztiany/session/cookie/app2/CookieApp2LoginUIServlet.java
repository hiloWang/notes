package com.ztiany.session.cookie.app2;

import com.ztiany.session.Constants;
import com.ztiany.session.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录界面：利用Cookie记住用户名
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.18 23:22
 */
public class CookieApp2LoginUIServlet extends HttpServlet {


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Utils.fixEncode(request, response);

        PrintWriter out = response.getWriter();
        //获取用户名，显示在表单中。（Cookie中）
        String username = "";
        String checked = "";

        Cookie cs[] = request.getCookies();
        for (int i = 0; cs != null && i < cs.length; i++) {
            if (Constants.LOGIN_INF.equals(cs[i].getName())) {
                username = cs[i].getValue();
                checked = "checked='checked'";
                break;
            }
        }

        //显示登录界面
        out.write("<form action='" + request.getContextPath() + "/servlet/CookieApp2LoginServlet' method='post'>");
        out.write("用户名:<input type='text' name='username' value='" + username + "'/><br/>");
        out.write("密码：<input type='password' name='password'/><br/>");
        out.write("<input type='checkbox' name='remember' " + checked + "/>记住用户名<br/>");
        out.write("<input type='submit' value='登录'/></form>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

}
