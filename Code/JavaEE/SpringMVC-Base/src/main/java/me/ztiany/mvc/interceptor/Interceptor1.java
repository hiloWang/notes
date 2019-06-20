package me.ztiany.mvc.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class Interceptor1 implements HandlerInterceptor {

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws Exception {
        System.out.println("方法前 1");
        //判断用户是否登陆，如果没有登陆重定向到登陆页面，如果登陆了就放行了

        // URL  http://localhost:8080/springmvc/login.action
       /* String requestURI = request.getRequestURI();
        if (!requestURI.contains("/login")) {
            String username = (String) request.getSession().getAttribute("USER_SESSION");
            if (null == username) {
                response.sendRedirect(request.getContextPath() + "/login.action");
                return false;
            }
        }*/

        return true;
    }

    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) {
        System.out.println("方法后 1");
    }

    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) {
        System.out.println("页面渲染后 1");

    }


}
