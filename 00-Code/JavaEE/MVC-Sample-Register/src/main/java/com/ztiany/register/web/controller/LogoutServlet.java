package com.ztiany.register.web.controller;

import com.ztiany.register.constants.Constants;
import com.ztiany.register.utils.CommonUtil;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.22 19:19
 */
public class LogoutServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CommonUtil.setUTF8Encode(request, response);

        request.getSession().removeAttribute(Constants.SESSION_USER);
        response.getWriter().write("注销成功");
        response.addHeader("Refresh", "3,URL=" + request.getContextPath());
    }
}
