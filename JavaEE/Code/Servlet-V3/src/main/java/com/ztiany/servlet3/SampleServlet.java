package com.ztiany.servlet3;

import java.io.IOException;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.4 23:22
 */

@WebServlet(value = {"/servlet/SampleServlet", "/servlet/SampleServlet11"},
        initParams = {
                @WebInitParam(name = "encoding", value = "UTF-8"),
                @WebInitParam(name = "XXX", value = "YYY")})
public class SampleServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Utils.setUTF8Encode(request, response);

        response.getWriter().write("你好啊");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

}
