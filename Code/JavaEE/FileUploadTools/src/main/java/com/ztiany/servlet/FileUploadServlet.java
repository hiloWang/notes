package com.ztiany.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 17.7.7 22:24
 */
public class FileUploadServlet extends HttpServlet {

    private static final long serialVersionUID = 2944809556494489970L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Utils.setUTF8Encode(request, response);

        try {
            boolean isMultipartContent = ServletFileUpload.isMultipartContent(request);
            if (!isMultipartContent) {
                response.getWriter().write("你的表单的enctype必须是multipart/form-data");
                return;
            }
            //创建DiskFileItemFactory对象:设置一些上传环境
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //factory.setRepository(new File("D:\\dev_file"));//改变临时文件的存放目录
            factory.setSizeThreshold(4 * 1024);//缓存大小

            ServletFileUpload sfu = new ServletFileUpload(factory);
            sfu.setFileSizeMax(10 * 1024 * 1024);//限制单个文件的大小
            sfu.setSizeMax(100 * 1024 * 1024);//限制总文件的大小

            @SuppressWarnings("unchecked")
            List<FileItem> items = sfu.parseRequest(request);

            for (FileItem item : items) {
                if (item.isFormField()) {
                    //如果是普通表单字段
                    processFormFiled(item);
                } else {
                    //处理文件
                    processUploadFile(item, request);
                }
            }

            response.getWriter().write("文件上传成功");

        } catch (FileUploadBase.FileSizeLimitExceededException e) {
            response.getWriter().write("upload file can not over 10M");
        } catch (FileUploadBase.SizeLimitExceededException e) {
            response.getWriter().write("all upload file can not over 100M");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void processUploadFile(FileItem item, @SuppressWarnings("unused") HttpServletRequest request) throws Exception {
        //设置上传文件的存储目录 放在WEB-INF目录下安全
        String directory = getServletContext().getRealPath("WEB-INF/files");
        File file = new File(directory);
        if (!file.exists()) {
            boolean mkdirs = file.mkdirs();//创建目录
            System.out.println("mkdirs:" + mkdirs);
        }

        String filedName = item.getName();//获取表单字段名 c:/a.txt  a.txt 根据浏览器不同而不同
        //为了保证上传文件不被以后上传文件 因为同名而被覆盖 应该给文件名前面加上唯一的UUID
        if (filedName != null) {

            filedName = UUID.randomUUID().toString() + "_" + FilenameUtils.getName(filedName);
            //同一个文件夹 里不能存储太多文件，否者不好管理
            int hashCode = filedName.hashCode();
            int dir1 = hashCode & 0XF;//一级目录，取最低四位，0~15共16个
            int dir2 = (hashCode & 0XF0) >> 4;//二级目录，取5-8位，0~15共16个
            File dir = new File(file, dir1 + "/" + dir2);
            if (!dir.exists()) {
                boolean mkdirs = dir.mkdirs();
                System.out.println("mkdirs:" + mkdirs);
            }
            //打印一下文件的类型
            String type = item.getContentType(); //IE会根据文件内容来判断文件类型
            System.out.println(type);
            //jpg后缀名的文本 得到的文件类型 还是文本类型 其他浏览器不一定
            //使用item.write方法，会自动删除临时文件
            item.write(new File(dir, filedName));
        }

    }

    private void processFormFiled(FileItem item) throws UnsupportedEncodingException {
        String filedName = item.getFieldName();
        String filedValue = item.getString("UTF-8");//Tomcat6/7默认使用IOS-8869-1查码表，这里设置位UTF-8
        System.out.println(filedName + ":" + filedValue);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

}
