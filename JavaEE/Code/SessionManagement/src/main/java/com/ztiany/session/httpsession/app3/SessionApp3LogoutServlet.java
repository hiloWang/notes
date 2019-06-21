package com.ztiany.session.httpsession.app3;

import com.ztiany.session.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionApp3LogoutServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Utils.fixEncode(request, response);

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        session.removeAttribute("user");
        out.write("注销成功.2秒后跳转到主页");
        response.setHeader("Refresh", "2;URL=" + request.getContextPath() + "/servlet/IndexServlet");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

}
