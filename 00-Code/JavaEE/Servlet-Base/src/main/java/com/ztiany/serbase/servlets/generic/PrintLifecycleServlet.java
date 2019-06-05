package com.ztiany.serbase.servlets.generic;

import java.io.IOException;

import javax.servlet.GenericServlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

//演示生命周期
public class PrintLifecycleServlet extends GenericServlet {

    public PrintLifecycleServlet() {
        System.out.println("调用了默认的构造方法");
    }

    public void init(ServletConfig config) {
        System.out.println("调用了初始化方法");
    }

    public void service(ServletRequest req, ServletResponse res)
            throws IOException {
        System.out.println("调用了服务方法");
        res.getOutputStream().write("SHEN ZHEN".getBytes());
    }

    public void destroy() {
        System.out.println("调用了销毁方法");
    }
}