package com.ztiany.servlet3;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet("/servlet/UploadServlet")
@MultipartConfig
public class UploadServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utils.setUTF8Encode(request, response);

        String name = request.getParameter("name");
        System.out.println(name);

        //上传字段
        Part photoPart = request.getPart("photo");
        //取文件名  Content-Disposition: form-data; name="photo"; filename="2.jpg"

        String value = photoPart.getHeader("Content-Disposition");
        int filenameIndex = value.indexOf("filename=") + 10;
        String filename = value.substring(filenameIndex, value.length() - 1);

        System.out.println(filename);
        photoPart.write(getServletContext().getRealPath("/WEB-INF/files") + "/" + filename);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
