package com.ztiany.session.cookie.app3;

import com.ztiany.session.Constants;
import com.ztiany.session.dao.BookDB;
import com.ztiany.session.domain.Book;
import com.ztiany.session.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//显示所有的商品,提供查看商品详细内容的链接
//显示用户最近的浏览历史记录，最多3条
public class CookieApp3ShowBooksServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Utils.fixEncode(request, response);

        PrintWriter out = response.getWriter();
        //显示所有的商品,提供查看商品详细内容的链接

        out.write("<h1>本站有以下好书:</h1>");
        Map<Integer, Book> books = BookDB.getAllBooks();

        for (Map.Entry<Integer, Book> me : books.entrySet()) {
            out.write(me.getValue().getName() + "&nbsp;&nbsp;<a target='_blank' href='" + request.getContextPath() + "/servlet/CookieApp3ShowDetailsServlet?id=" + me.getKey() + "'>详细</a><br/>");
        }


        //显示用户最近的浏览历史记录：3条  bookHistory=1-2-3
        out.write("<hr/>");
        out.write("您最近浏览过的商品：<br/>");
        Cookie cs[] = request.getCookies();
        for (int i = 0; cs != null && i < cs.length; i++) {
            if (Constants.VISIT_HISTORY.equals(cs[i].getName())) {
                String value = cs[i].getValue();//1-2-3
                String ids[] = value.split("-");
                for (String bookId : ids) {
                    Book book = BookDB.getBookById(Integer.parseInt(bookId));
                    out.write(book.getName() + "<br/>");
                }
                break;
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

}
