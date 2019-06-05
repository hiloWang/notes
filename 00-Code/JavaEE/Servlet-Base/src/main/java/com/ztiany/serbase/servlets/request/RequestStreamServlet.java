package com.ztiany.serbase.servlets.request;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 以输入流的形式获取请求正文内容
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.16 0:22
 */
public class RequestStreamServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream in = request.getInputStream();
        int len;
        byte b[] = new byte[1024];
        while ((len = in.read(b)) != -1) {
            System.out.println(new String(b, 0, len));
        }
        in.close();
    }
}
