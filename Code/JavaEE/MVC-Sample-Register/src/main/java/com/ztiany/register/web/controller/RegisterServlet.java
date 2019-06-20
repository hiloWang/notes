package com.ztiany.register.web.controller;

import com.ztiany.register.constants.Constants;
import com.ztiany.register.domain.User;
import com.ztiany.register.exception.UserExistException;
import com.ztiany.register.service.BusinessService;
import com.ztiany.register.service.impl.BusinessServiceImpl;
import com.ztiany.register.utils.CommonUtil;
import com.ztiany.register.web.bean.UserRegisterFormBean;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.22 19:18
 */
public class RegisterServlet extends HttpServlet {

    private final BusinessService mBusinessService = new BusinessServiceImpl();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        CommonUtil.setUTF8Encode(request, response);

        //step 1 获取数据并封装到FormBean中
        UserRegisterFormBean userRegisterFormBean = CommonUtil.fillBeanFromRequest(request, UserRegisterFormBean.class);

        //step 2 验证表单数据
        boolean validate = userRegisterFormBean.validate();
        if (!validate) {//数据不合法
            request.setAttribute(Constants.REQUEST_FORM_BEAN, userRegisterFormBean);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/register.jsp");
            requestDispatcher.forward(request, response);
            return;
        }

        try {
            //step 3 拷贝数据到domain中，并调用业务对象进行注册
            User user = new User();
            CommonUtil.copyProperties(user, userRegisterFormBean);
            mBusinessService.register(user);

            response.getWriter().write("注册成功,2秒后转向主页");
            response.setHeader("Refresh", "2;URL=" + request.getContextPath());//配置了默认页面为主页

        } catch (UserExistException e) {
            //用户已存在
            userRegisterFormBean.getErrors().put("username", "用户名已存在");
            request.setAttribute(Constants.REQUEST_FORM_BEAN, userRegisterFormBean);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("/register.jsp");
            requestDispatcher.forward(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
