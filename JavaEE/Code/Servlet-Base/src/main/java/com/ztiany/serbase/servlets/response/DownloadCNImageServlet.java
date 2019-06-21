package com.ztiany.serbase.servlets.response;

import com.ztiany.serbase.utils.LogUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 下载中文名文件，记得使用URLEncoder编码
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.15 23:28
 */
public class DownloadCNImageServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //得到文件的真实路径
        String realPath = getServletContext().getRealPath("/images/美女.jpg");
        //截取文件名
        String filename = realPath.substring(realPath.lastIndexOf(File.separator) + 1);//妮子.jpg
        LogUtils.LOG.info("下载文件名为：" + filename);
        //通知浏览器以下载的方式打开，中文文件名文件下载有问题：如果不使用URL编码文件名没有了
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(filename, "UTF-8"));
        response.setContentType("application/octet-stream");
        //下载
        OutputStream out = response.getOutputStream();
        try (InputStream in = new FileInputStream(realPath)) {
            int len;
            byte b[] = new byte[1024];
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
            in.close();
        }
        //不需要关闭out，会自动关闭
    }
}
