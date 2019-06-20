package com.ztiany.serbase.servlets.request;

import com.ztiany.serbase.domain.User;

import org.apache.commons.beanutils.BeanUtils;

import java.beans.PropertyDescriptor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 借助BeanUtils框架，User的password数据参数如何解决
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.16 0:28
 */
public class BeanUtilsServlet extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        test6(request, response);
    }

    //封装数据到JavaBean，借助BeanUtils框架
    @SuppressWarnings("unused,unchecked")
    private void test6(HttpServletRequest request, HttpServletResponse response) {
        User user = new User();
        System.out.println("封装前：" + user);
        try {
            BeanUtils.populate(user, request.getParameterMap());
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("封装后：" + user);
    }

    //封装数据到JavaBean:借助BeanUtils框架
    @SuppressWarnings("unused,unchecked")
    private void test5(HttpServletRequest request, HttpServletResponse response) {
        User user = new User();
        System.out.println("封装前：" + user);
        Map<String, String[]> map = request.getParameterMap();//返回一个Map。key：String类型，请求参数名称。value：String[],请求参数的值
        for (Map.Entry<String, String[]> me : map.entrySet()) {
            String paramName = me.getKey();
            String paramValues[] = me.getValue();
            try {
                BeanUtils.setProperty(user, paramName, paramValues);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("封装后：" + user);
    }

    //封装数据到JavaBean
    @SuppressWarnings("unused,unchecked,all")
    private void test4(HttpServletRequest request, HttpServletResponse response) {
        User user = new User();
        System.out.println("封装前：" + user);
        Map<String, String[]> map = request.getParameterMap();//返回一个Map。key：String类型，请求参数名称。value：String[],请求参数的值
        for (Map.Entry<String, String[]> me : map.entrySet()) {
            String paramName = me.getKey();
            String paramValues[] = me.getValue();
            try {
                PropertyDescriptor pd = new PropertyDescriptor(paramName, User.class);
                Method writerMethod = pd.getWriteMethod();//得到setter方法
                writerMethod.invoke(user, (Object) paramValues);
                //todo fix it
                if (paramValues.length == 1) {
                    writerMethod.invoke(user, paramValues);
                } else {
                    //writerMethod.invoke(user, (Object)paramValues);//方法一
                    writerMethod.invoke(user, new Object[]{paramValues});//方法一
                }

            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        System.out.println("封装后：" + user);
    }

    //获取所有的请求参数名和值，封装到JavaBean中，约定优于编码
    @SuppressWarnings("unused,unchecked")
    private void test31(HttpServletRequest request, HttpServletResponse response) {
        //表单的输入域名称和JavaBean的属性一致（约定优于编码）
        User user = new User();
        System.out.println("封装前：" + user);
        Enumeration<String> e = request.getParameterNames();//参数名称
        while (e.hasMoreElements()) {
            String paramName = e.nextElement();//参数名
            String paramValues[] = request.getParameterValues(paramName);
            //内省：属性描述器设置值
            try {
                PropertyDescriptor pd = new PropertyDescriptor(paramName, User.class);
                Method writerMethod = pd.getWriteMethod();//得到setter方法
                //todo fix it
                if (paramValues.length == 1) {
                    writerMethod.invoke(user, paramValues);
                } else {
                    //writerMethod.invoke(user, (Object)paramValues);//方法一
                    writerMethod.invoke(user, new Object[]{paramValues});//方法一
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        System.out.println("封装后：" + user);
    }

    //获取所有的请求参数名和值，打印出来没有用的
    @SuppressWarnings("unused")
    private void test3(HttpServletRequest request, HttpServletResponse response) {
        @SuppressWarnings("unchecked")
        Enumeration<String> e = request.getParameterNames();//参数名称
        while (e.hasMoreElements()) {
            String paramName = e.nextElement();//参数名
            String paramValues[] = request.getParameterValues(paramName);
            System.out.println(paramName + "=" + Arrays.asList(paramValues));
        }
    }

    //获取重名的请求参数值
    private void test2(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html;charset=UTF-8");
        //用户的所有输入都是字符串
        String username = request.getParameter("username");
        String passwords[] = request.getParameterValues("password");
        System.out.println(username + ":" + Arrays.asList(passwords));
        if (passwords.length > 1) {
            if (!passwords[0].equals(passwords[1])) {
                response.getWriter().write("两次密码不一致，请重新输入。2秒后自动跳转");
                response.setHeader("Refresh", "2;URL=http://www.google.cn");
            }
        }
    }

    //获取单一的请求参数值
    private void test1(HttpServletRequest request) {
        //用户的所有输入都是字符串
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        System.out.println(username + ":" + password);
    }
}
