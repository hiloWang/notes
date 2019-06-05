package com.ztiany.session.httpsession.app4;

import com.ztiany.session.Constants;
import com.ztiany.session.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SessionApp4RegisterServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Utils.fixEncode(request, response);

        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String ptoken = request.getParameter("token");//表单

        HttpSession session = request.getSession();
        String stoken = (String) session.getAttribute(Constants.TOKEN);

        //判断token，是否为第一次标识
        if (ptoken != null && ptoken.equals(stoken)) {
            //如果是则让其进入，并且只让其进入一次
            System.out.println(username);
            session.removeAttribute(Constants.TOKEN);
        } else {
            //否则说明是重复提交
            out.write("请不要重复提交");
        }

        response.getWriter().write("<a href='" + request.getContextPath() + "/servlet/SessionApp4RegisterUIServlet'>register go on</a>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

}
