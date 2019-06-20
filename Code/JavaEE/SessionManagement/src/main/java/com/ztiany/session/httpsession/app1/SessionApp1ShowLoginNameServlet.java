package com.ztiany.session.httpsession.app1;

import com.ztiany.session.utils.Utils;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import static com.ztiany.session.Constants.STORE_NAME;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.18 23:47
 */
public class SessionApp1ShowLoginNameServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utils.fixEncode(req, resp);
        HttpSession session = req.getSession();
        String attribute = (String) session.getAttribute(STORE_NAME);
        if (attribute != null) {
            resp.getWriter().write("保存的登录名为：" + attribute);
        }
    }
}
