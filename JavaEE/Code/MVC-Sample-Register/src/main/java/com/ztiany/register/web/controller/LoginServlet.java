package com.ztiany.register.web.controller;

import com.ztiany.register.constants.Constants;
import com.ztiany.register.domain.User;
import com.ztiany.register.service.BusinessService;
import com.ztiany.register.service.impl.BusinessServiceImpl;
import com.ztiany.register.utils.CommonUtil;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.22 19:17
 */
public class LoginServlet extends HttpServlet {

    private final BusinessService mBusinessService = new BusinessServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CommonUtil.setUTF8Encode(request, response);

        String username = request.getParameter(Constants.FORM_KEY_USERNAME);
        String password = request.getParameter(Constants.FORM_KEY_PASSWORD);

        User user = mBusinessService.login(username, password);
        if (user == null) {
            response.getWriter().write("用户名或密码错误，请重新登录");
            response.addHeader("REFRESH", "2,URL=" + request.getContextPath() + "/login.jsp");
        } else {
            response.getWriter().write("登录成功");
            request.getSession().setAttribute(Constants.SESSION_USER, user);
            response.addHeader("REFRESH", "3,URL=" + request.getContextPath());
        }
    }
}
