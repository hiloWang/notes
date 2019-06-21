package com.ztiany.session.httpsession.app2;

import com.ztiany.session.dao.BookDB;
import com.ztiany.session.domain.Book;
import com.ztiany.session.utils.Utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 利用Session保存用户端购物车记录
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.18 23:56
 */
public class SessionApp2BooksServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Utils.fixEncode(req, resp);

        PrintWriter out = resp.getWriter();
        out.write("<h1>本站有以下好书</h1>");
        Map<Integer, Book> books = BookDB.getAllBooks();

        for (Map.Entry<Integer, Book> me : books.entrySet()) {
            out.write(me.getValue().getName() + "&nbsp;&nbsp;<a href='" + req.getContextPath() + "/servlet/SessionApp2BuyServlet?bookId=" + me.getKey() + "'>放入购物车</a><br/>");
        }

        out.write("<hr/><a href='"+req.getContextPath()+"/servlet/SessionApp2ShowCartServlet'>查看购车</a>");
    }
}
