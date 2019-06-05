package com.ztiany.serbase.servlets.http;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 演示让浏览器以下载方式读取数据
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.14 17:10
 */
public class DownloadServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //通知浏览器以下载的方式打开
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment;filename=android.jpg");

        //通过ServletContext的getRealPath获取文件相对路径：相对于应用来说，是固定的
        //构建文件的输入流
        String realPath = getServletContext().getRealPath("/images/android.jpg");
        InputStream in = new FileInputStream(realPath);

        //用response对象的输出流输出
        OutputStream out = response.getOutputStream();
        int len;
        byte b[] = new byte[1024];
        while ((len = in.read(b)) != -1) {
            out.write(b, 0, len);
        }

        //关闭资源
        in.close();
        out.close();
    }
}
