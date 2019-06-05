package com.ztiany.session.cookie.app3;

import com.ztiany.session.Constants;
import com.ztiany.session.dao.BookDB;
import com.ztiany.session.domain.Book;
import com.ztiany.session.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 显示商品的详情，向客户端写Cookie，记住最近的浏览记录(关键)
 */
public class CookieApp3ShowDetailsServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Utils.fixEncode(request, response);

        PrintWriter out = response.getWriter();
        //显示商品的详情
        out.write("书籍详情：<br/>");
        String bookId = request.getParameter("id");
        Book book = BookDB.getBookById(Integer.parseInt(bookId));
        out.write(book.toString());
        //向客户端写Cookie，记住最近的浏览记录


        String makeIds = makeIds(request, bookId);//1-2-3
        Cookie c = new Cookie(Constants.VISIT_HISTORY, makeIds);
        c.setPath(request.getContextPath());
        c.setMaxAge(Integer.MAX_VALUE);
        response.addCookie(c);
    }

    /*
     *  客户端带来的cookie，当前访问的书籍的id，应该写回的id
     *  Cookie cs[]=null   1      bookHistory=1
     *  没有bookHistoryCookie   1      bookHistory=1
     *  bookHistory=1    2           bookHistory=2-1
     *  bookHistory=1-2    2        bookHistory=2-1
     *  bookHistory=1-2    3        bookHistory=3-1-2
     *  bookHistory=1-2-3   4      bookHistory=4-1-2
     *  bookHistory=1-2-3   2      bookHistory=2-1-3
     */
    private String makeIds(HttpServletRequest request, String bookId) {
        Cookie cs[] = request.getCookies();
        if (cs == null) {
            return bookId;
        }
        //--------------------
        Cookie bookHistoryCookie = null;
        for (Cookie c : cs) {
            if (Constants.VISIT_HISTORY.equals(c.getName())) {
                bookHistoryCookie = c;
                break;
            }
        }

        if (bookHistoryCookie == null) {
            return bookId;
        }

        String value = bookHistoryCookie.getValue();// 1-2-3
        String ids[] = value.split("-");
        LinkedList<String> list = new LinkedList<>(Arrays.asList(ids));

        if (list.size() < 3) {
            if (list.contains(bookId)) {
                list.remove(bookId);
            }
        } else {
            if (list.contains(bookId)) {
                list.remove(bookId);
            } else {
                list.removeLast();
            }
        }

        list.addFirst(bookId);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) {
                sb.append("-");
            }
            sb.append(list.get(i));
        }

        return sb.toString();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
