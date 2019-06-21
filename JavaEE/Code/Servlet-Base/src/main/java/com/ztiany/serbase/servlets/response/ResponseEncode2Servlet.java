package com.ztiany.serbase.servlets.response;

import com.ztiany.serbase.utils.LogUtils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 使用字符流输出中文数据
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.15 23:21
 */
public class ResponseEncode2Servlet extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        test2(response);
    }

    //经常用的
    private void test3(HttpServletResponse response) throws IOException {
        //更改字符流使用的码表为UTF-8，并且通知浏览器用UTF-8进行显示
        response.setContentType("text/html;charset=UTF-8");
        String data = "东莞和深圳不远，大家经常去";
        PrintWriter out = response.getWriter();
        out.write(data);
    }

    //更改字符流使用的编码
    private void test2(HttpServletResponse response) throws IOException {
        //更改字符流使用的码表为UTF-8
        String encoding = response.getCharacterEncoding();
        if (!encoding.equals("UTF-8")) {
            response.setCharacterEncoding("UTF-8");
        }
        //通知浏览器用UTF-8进行显示(起始这个方法就够了)
        response.setContentType("text/html;charset=UTF-8");
        String data = "东莞和深圳很近";
        PrintWriter out = response.getWriter();
        out.write(data);
    }

    //字符流使用的默认编码与服务器有关（Tomcat6、7默认用的ISO-8859-1，Tomcat8默认是UTF-8）
    //如果是Tomcat6、7就会乱码
    private void test1(HttpServletResponse response) throws IOException {
        LogUtils.LOG.info("response.getCharacterEncoding() = " + response.getCharacterEncoding());
        String data = "东莞和深圳很近";
        PrintWriter out = response.getWriter();
        out.write(data);//查的是什么码表？
    }

}
