package com.ztiany.servlet3;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.AsyncContext;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(value = "/servlet/RegisterServlet", asyncSupported = true)
public class RegisterServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        out.write("开始保存信息....<br/>");
        out.flush();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        out.write("保存成功，开始发送激活邮件<br/>");
        out.flush();

        //单独启动一个线程
        AsyncContext ac = request.startAsync();//开启异步
        SendMail sm = new SendMail(ac);
        sm.start();


        out.write("全部执行成功....<br/>");
        out.flush();
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

}
