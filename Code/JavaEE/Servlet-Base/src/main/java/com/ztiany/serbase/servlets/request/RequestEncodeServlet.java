package com.ztiany.serbase.servlets.request;

import com.ztiany.serbase.domain.User;

import org.apache.commons.beanutils.BeanUtils;

import java.io.IOException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 中文请求参数的编码，建议使用POST
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.16 0:18
 */
public class RequestEncodeServlet extends HttpServlet {

    /*
    post请求正文编码解决方案
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //请求
        //request中的编码取决于客户端使用的编码，更改程序默认查询的码表(对请求正文)
        request.setCharacterEncoding("UTF-8");
        User user = new User();
        try {
            BeanUtils.populate(user, request.getParameterMap());
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(request.getContentType());//可能为null
        System.out.println(request.getCharacterEncoding());//可能为null

        //-------------------------------------华丽的分界线（关键），获取请求与写响应是不相干的

        //响应
        response.setContentType("text/html;charset=GBK");
        response.getWriter().write(user.toString());
    }


    /*
    get请求正文编码解决方案，get方式请求参数放在url连接后面：
    tomcat 6 iso-8859-1
    tomcat 7 utf-8
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(request.getContentType());//可能为null
        System.out.println(request.getCharacterEncoding());//可能为null

        String username = request.getParameter("username");//ISO-8859-1
        byte b[] = username.getBytes("ISO-8859-1");
        username = new String(b, "UTF-8");
        System.out.println(username);
    }
}
