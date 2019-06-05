package com.ztiany.session.httpsession.app3;

import com.ztiany.session.Constants;
import com.ztiany.session.dao.UserDB;
import com.ztiany.session.domain.User;
import com.ztiany.session.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//完成登录
public class SessionApp3LoginServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Utils.fixEncode(request, response);

        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession();
        //验证码验证
        String pcode = request.getParameter("code");//表单数据
        String scode = (String) session.getAttribute(Constants.IMAGE_CODE);//会话中的

        if (!pcode.equals(scode)) {
            out.write("验证码有误，请重新输入.2秒后跳转到登录页面");
            response.setHeader("Refresh", "2;URL=" + request.getContextPath() + "/servlet/SessionApp3LoginUIServlet");
            return;
        }

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = UserDB.findUser(username, password);
        if (user == null) {
            out.write("错误的用户名或密码.2秒后跳转到登录页面");
            response.setHeader("Refresh", "2;URL=" + request.getContextPath() + "/servlet/SessionApp3LoginUIServlet");
            return;
        }

        //登录成功
        session.setAttribute(Constants.USER_SESSION, user);
        out.write("登录成功.2秒后跳转到主页");
        response.setHeader("Refresh", "2;URL=" + request.getContextPath() + "/servlet/SessionApp3IndexServlet");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

}
