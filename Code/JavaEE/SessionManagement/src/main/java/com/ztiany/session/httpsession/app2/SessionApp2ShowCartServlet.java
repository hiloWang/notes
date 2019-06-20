package com.ztiany.session.httpsession.app2;

import com.ztiany.session.Constants;
import com.ztiany.session.domain.Book;
import com.ztiany.session.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.19 0:11
 */
public class SessionApp2ShowCartServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utils.fixEncode(req, resp);

        HttpSession session = req.getSession(false);
        System.out.println("session：" + session);
        if (session == null) {
            resp.getWriter().write("您还没有购买过任何书籍");
            return;
        }

        ArrayList<Book> bookList = (ArrayList<Book>) session.getAttribute(Constants.CART_LIST);
        System.out.println("bookList：" + bookList);
        if (bookList == null) {
            resp.getWriter().write("还没有添加书籍到购物车");
            return;
        }

        resp.getWriter().write("您的购物车有以下书籍：<br/>");
        for (Book book : bookList) {
            resp.getWriter().write(book.toString() + "<br/>");
        }

        resp.getWriter().write("清空购物车：<a href='" + req.getContextPath() + "/servlet/SessionApp2ClearCartServlet'>清空购物车</a>");
    }
}
