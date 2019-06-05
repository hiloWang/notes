package com.ztiany.servlet;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 把WEB-INF/files及其子目录下的文件遍历出来，封装数据交给JSP去显示
 */
public class ShowFilesServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Utils.setUTF8Encode(request, response);


        String realDirectoryPath = getServletContext().getRealPath("/WEB-INF/files");
        File file = new File(realDirectoryPath);

        Map<String, String> map = new HashMap<>();
        treeWalk(file, map);//递归遍历文件名

        request.setAttribute("map", map);
        request.getRequestDispatcher("/listFiles.jsp").forward(request, response);
    }

    //遇到文件，把文件名拆分，放到map中
    private void treeWalk(File file, Map<String, String> map) {

        if (file.isDirectory()) {
            File childFile[] = file.listFiles();
            if (childFile != null) {
                for (File f : childFile) {
                    treeWalk(f, map);
                }
            }
        } else {
            String filename = file.getName();//UUID_filename，去掉UUID
            String oldFilename = filename.substring(filename.indexOf("_") + 1);
            map.put(filename, oldFilename);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

}
