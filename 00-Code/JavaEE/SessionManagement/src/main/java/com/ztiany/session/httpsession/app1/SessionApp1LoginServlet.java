package com.ztiany.session.httpsession.app1;

import com.ztiany.session.utils.Utils;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.ztiany.session.Constants.STORE_NAME;

/**
 * 登录连接：http://localhost:8080/SessionManagement/servlet/SessionApp1LoginServlet?name=xxx，利用Session记住登录名
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.18 23:41
 */
public class SessionApp1LoginServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utils.fixEncode(req, resp);
        String name = req.getParameter("name");
        if (Objects.isNull(name) || "".equals(name)) {
            resp.getWriter().write("请求方式为：http://localhost:8080/SessionManagement/servlet/SessionApp1LoginServlet?name=xxx");
            return;
        }

        HttpSession session = req.getSession();
        session.setAttribute(STORE_NAME, name);
        resp.getWriter().write("您的登录已经保存成功，<a href='" + req.getContextPath() + "/servlet/SessionApp1ShowLoginNameServlet'>查看</a>");


    }
}
