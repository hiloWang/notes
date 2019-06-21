package com.ztiany.session.cookie.app1;

import com.ztiany.session.utils.Utils;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.ztiany.session.Constants.LAST_ACCESS_TIME;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.18 23:10
 */
public class ClearLastAccessTimeServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utils.fixEncode(req, resp);

        Cookie cookie = new Cookie(LAST_ACCESS_TIME, new Date().toString());
        //设置cookie 的路径为应用路径
        cookie.setPath(req.getContextPath());
        cookie.setMaxAge(0);
        resp.addCookie(cookie);

        resp.getWriter().write("清除成功");
    }
}
