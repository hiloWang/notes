package com.ztiany.mall.web.servlet;

import com.ztiany.mall.config.AppConfig;
import com.ztiany.mall.exception.AppException;
import com.ztiany.mall.utils.LogUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.16 23:44
 */
public class AbsBaseServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {

        //1、获得请求的method的名称
        String action = req.getParameter(AppConfig.ACTION);

        try {
            //2、获得当前被访问的对象的字节码对象
            Class<?> clazz = this.getClass();//ProductServlet.class ---- UserServlet.class
            //3、获得当前字节码对象的中的指定方法
            Method method = clazz.getMethod(action, HttpServletRequest.class, HttpServletResponse.class);
            //4、执行相应功能方法
            method.invoke(this, req, resp);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            LogUtils.error(this, e);
            throw new AppException(this.getClass().getName() + " 中没有找到可用的方法：" + action);
        } catch (InvocationTargetException e) {
            LogUtils.error(this, e);
            throw new AppException(this.getClass().getName() + " 调用目标方法异常：" + action);
        } catch (Exception e) {
            LogUtils.error(this, e);
            throw e;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        doGet(req, resp);
    }

}
