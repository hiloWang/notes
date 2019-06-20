package com.ztiany.session.httpsession.app2;

import com.ztiany.session.Constants;
import com.ztiany.session.utils.Utils;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.19 0:17
 */
public class SessionApp2ClearCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utils.fixEncode(req, resp);

        HttpSession session = req.getSession();
        session.removeAttribute(Constants.CART_LIST);

        resp.sendRedirect(req.getContextPath() + "/servlet/SessionApp2BooksServlet");
    }
}
