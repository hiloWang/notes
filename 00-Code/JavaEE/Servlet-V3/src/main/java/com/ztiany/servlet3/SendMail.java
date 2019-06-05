package com.ztiany.servlet3;

import java.io.PrintWriter;

import javax.servlet.AsyncContext;

public class SendMail extends Thread {

    private AsyncContext ac;

    SendMail(AsyncContext ac) {
        this.ac = ac;
    }

    public void run() {
        //发送邮件
        try {
            Thread.sleep(3000);

            PrintWriter out = ac.getResponse().getWriter();
            out.write("邮件发送<br/>");
            out.flush();
            ac.complete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
