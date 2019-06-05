package com.ztiany.servlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DownloadServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Utils.setUTF8Encode(request, response);

        //获取用户提交过来的要下载的文件名UUID文件名
        String filename = request.getParameter("filename");//get请求
        filename = new String(filename.getBytes("ISO-8859-1"), "UTF-8");

        //计算文件的存放路径，之前存放路径的算法
        String realDirectoryPath = getServletContext().getRealPath("/WEB-INF/files");
        int hashCode = filename.hashCode();

        int dir1 = hashCode & 0xf;//一级目录
        int dir2 = (hashCode & 0xf0) >> 4;//二级目录

        //判断文件还在否
        File file = new File(realDirectoryPath + "/" + dir1 + "/" + dir2 + "/" + filename);

        if (!file.exists()) {
            response.getOutputStream().write("您下载的文件可能已经大概被删除了".getBytes("UTF-8"));
            return;
        }

        //开始下载
        String oldFilename = filename.substring(filename.indexOf("_") + 1);//原来的文件名
        response.setHeader("Content-Disposition", "attachment;filename=" +
                URLEncoder.encode(oldFilename, "UTF-8"));//文件如果是中文，要URL编码

        InputStream in = new FileInputStream(file);
        OutputStream out = response.getOutputStream();
        int len;
        byte b[] = new byte[1024];
        while ((len = in.read(b)) != -1) {
            out.write(b, 0, len);
        }
        in.close();
        out.write("下载成功!".getBytes("UTF-8"));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

}
