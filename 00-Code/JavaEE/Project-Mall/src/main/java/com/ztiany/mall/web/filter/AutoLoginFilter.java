package com.ztiany.mall.web.filter;

import com.ztiany.mall.config.AppConfig;
import com.ztiany.mall.domain.User;
import com.ztiany.mall.service.UserService;
import com.ztiany.mall.utils.BeanFactory;
import com.ztiany.mall.utils.LogUtils;
import com.ztiany.mall.utils.StringChecker;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.16 23:51
 */
public class AutoLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) {
        //no op
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        User user = (User) req.getSession().getAttribute(AppConfig.USER);

        if (user == null) {

            String cookie_username = null;
            String cookie_password = null;

            //获取携带用户名和密码cookie
            Cookie[] cookies = req.getCookies();

            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    //获得想要的cookie
                    if (AppConfig.COOKIE_USERNAME.equals(cookie.getName())) {
                        cookie_username = cookie.getValue();
                    }
                    if (AppConfig.COOKIE_PASSWORD.equals(cookie.getName())) {
                        cookie_password = cookie.getValue();
                    }
                }
            }

            if (!StringChecker.isEmpty(cookie_username) && !StringChecker.isEmpty(cookie_password)) {
                //去数据库校验该用户名和密码是否正确
                UserService service = BeanFactory.getUserService();
                try {
                    user = service.autoLogin(cookie_username, cookie_password);
                } catch (SQLException e) {
                    e.printStackTrace();
                    LogUtils.error(this, e);
                }

                //完成自动登录
                req.getSession().setAttribute(AppConfig.USER, user);
            }
        }

        //放行
        chain.doFilter(req, response);
    }

    @Override
    public void destroy() {
        //no op
    }
}
