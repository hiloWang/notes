package com.ztiany.serbase.servlets.http;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 演示Gzip
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.15 0:03
 */
public class GzipServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doGet(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String data = "where \"$CATALINA_HOME\" is the root of the Tomcat installation directory. If you're seeing this page, and you don't think you should be, " +
                "then you're either a user who has arrived at new installation of Tomcat, or you're an administrator who hasn't got his/her setup quite right. " +
                "Providing the latter is the case, please refer to the Tomcat Documentation for more detailed setup and " +
                "administration information than is found in the INSTALL file.";

        byte b[] = data.getBytes();
        System.out.println("压缩前大小：" + b.length);

        //用gzip压缩数据
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();//内存字节缓存流
        GZIPOutputStream gout = new GZIPOutputStream(byteArrayOutputStream);//用GZIPOutputStream写的数据，会被压缩到指定的流中
        gout.write(b);
        gout.close();

        //取出缓存的内容（已经是压缩后的）
        b = byteArrayOutputStream.toByteArray();
        System.out.println("压缩后大小：" + b.length);

        //通知浏览器，响应正文的MIME类型
        resp.setHeader("Content-Type", "text/html");
        resp.setHeader("Content-Encoding", "gzip");
        resp.setHeader("Content-Length", String.valueOf(b.length));
        resp.getOutputStream().write(b);
    }
}
