package com.ztiany.serbase.servlets.response;

import com.ztiany.serbase.utils.LogUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.15 23:49
 */
public class RegisterServlet extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        @SuppressWarnings("unchecked")
        Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((key, value) -> LogUtils.LOG.info("登录参数：" + key + " = " + Arrays.toString(value)));

        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("注册成功，2秒后自动转向登录页面");
        response.setHeader("Refresh", "2;URL=http://www.google.cn");
    }
}
