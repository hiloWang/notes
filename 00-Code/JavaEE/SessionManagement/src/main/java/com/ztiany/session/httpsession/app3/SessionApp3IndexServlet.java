package com.ztiany.session.httpsession.app3;

import com.ztiany.session.Constants;
import com.ztiany.session.domain.User;
import com.ztiany.session.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Session技术记住登录用户和验证码校验
 */
public class SessionApp3IndexServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Utils.fixEncode(request, response);

        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Constants.USER_SESSION);

        if (user == null) {
            out.write("<a href='" + request.getContextPath() + "/servlet/SessionApp3LoginUIServlet'>登录</a>");
        } else {
            out.write("欢迎您：" + user.getNickname() + "<a href='" + request.getContextPath() + "/servlet/SessionApp3LogoutServlet'>注销</a>");
        }

        out.write("<hr/>");
        out.write("<h1>这是主页</h1>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

}
