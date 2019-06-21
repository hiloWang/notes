package com.ztiany.serbase.servlets.response;

import com.ztiany.serbase.utils.LogUtils;

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用字节流输出中文(这里加上默认编码为GBK，而浏览器使用的编码也是GBK)
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.15 23:03
 */
public class ResponseEncode1Servlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        LogUtils.LOG.info("默认编码：" + Charset.defaultCharset());
        test2(response);
    }


    private void test4(HttpServletResponse response) throws IOException {
        int data = 97;
        ServletOutputStream out = response.getOutputStream();
        out.write((data + "").getBytes());
    }

    private void test3(HttpServletResponse response) throws IOException {
        //写出的是ASCII编码
        int data = 97;
        ServletOutputStream out = response.getOutputStream();
        out.write(data);
    }

    //字节流输出字符：有乱码的解决方案
    private void test21(HttpServletResponse response) throws IOException {
        //输出meta内容:方式一（不建议使用），原理：http-equiv相当于响应头，这里告知客户端数据使用UTF-8
        //out.write("<meta http-equiv='Content-Type' content='text/html;charset=UTF-8'>".getBytes());
        //设置响应消息头：方式二，原理响应头
        //response.setHeader("Content-Type", "text/html;charset=UTF-8");
        //方式三：建议写在代码的前面
        response.setContentType("text/html;charset=UTF-8");
        String data = "东莞";
        byte b[] = data.getBytes("UTF-8");//使用UTF-8编码
        ServletOutputStream out = response.getOutputStream();
        out.write(b);
    }

    //字节流输出字符：字符用默认编码， 结果有乱码
    private void test2(HttpServletResponse response) throws IOException {
        String data = "深圳";
        byte b[] = data.getBytes("UTF-8");//使用UTF-8编码
        ServletOutputStream out = response.getOutputStream();
        out.write(b);
    }


    //字节流输出字符：字符用默认编码，没有乱码
    private void test1(HttpServletResponse response) throws IOException {
        String data = "深圳";
        byte b[] = data.getBytes();//查找默认的编码：GBK
        ServletOutputStream out = response.getOutputStream();
        out.write(b);
    }

}
