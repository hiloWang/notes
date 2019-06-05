package com.ztiany.session.httpsession.app4;

import com.ztiany.session.Constants;
import com.ztiany.session.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 防止表单重复提交：
 * 方式一：JS（不推荐）
 * 方式二：利用UUID和Session
 */
public class SessionApp4RegisterUIServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Utils.fixEncode(request, response);

        PrintWriter out = response.getWriter();

        //添加一个唯一标识，用于标识此次请求
        String token = UUID.randomUUID().toString();
        request.getSession().setAttribute(Constants.TOKEN, token);

        out.write("<form action='" + request.getContextPath() + "/servlet/SessionApp4RegisterServlet' method='post'>");
        out.write("username:<input name='username'/><br/>");
        out.write("<input type='hidden' name='token' value='" + token + "'/><br/>");
        out.write("<input id='bt1' type='submit' value='注册'/></form>");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

}
