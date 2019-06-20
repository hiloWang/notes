package com.ztiany.session.httpsession.app2;

import com.ztiany.session.Constants;
import com.ztiany.session.dao.BookDB;
import com.ztiany.session.domain.Book;
import com.ztiany.session.utils.Utils;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.18 23:58
 */
public class SessionApp2BuyServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utils.fixEncode(req, resp);

        String bookId = req.getParameter(Constants.BOOK_ID);
        Book book = BookDB.getBookById(Integer.parseInt(bookId));

        HttpSession session = req.getSession();

        ArrayList<Book> cartList;

        if (session.getAttribute(Constants.CART_LIST) != null) {
            cartList = (ArrayList<Book>) session.getAttribute(Constants.CART_LIST);
        } else {
            cartList = new ArrayList<>();
            session.setAttribute(Constants.CART_LIST, cartList);
        }

        boolean isAdded = false;
        for (Book item : cartList) {
            if (item.getId() == book.getId()) {
                isAdded = true;
                break;
            }
        }

        if (!isAdded) {
            cartList.add(book);
        }

        resp.getWriter().write("购买成功：<a href='" + req.getContextPath() + "/servlet/SessionApp2BooksServlet'>继续购物</a>");

        //session的cookie生命周期为内存，这里让cookie可以保存在文件缓存中
        Cookie cookie = new Cookie(Constants.J_SESSION_ID, session.getId());//session cookie的固有形式
        cookie.setMaxAge(60 * 60 * 24);
        cookie.setPath(req.getContextPath());
        resp.addCookie(cookie);
    }
}
