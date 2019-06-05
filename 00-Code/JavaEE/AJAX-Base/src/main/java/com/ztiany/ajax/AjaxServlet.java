package com.ztiany.ajax;

import java.io.IOException;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.13 14:12
 */
@WebServlet(name = "AjaxServlet", urlPatterns = "/servlet/AjaxServlet")
public class AjaxServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        CommonUtil.setUTF8Encode(request, response);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        response.getWriter().write("{\"code\":200,\"message\":\"success\",\"result\":\"操作成功\"}");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }
}
