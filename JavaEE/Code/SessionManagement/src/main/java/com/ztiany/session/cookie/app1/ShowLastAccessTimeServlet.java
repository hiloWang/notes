package com.ztiany.session.cookie.app1;

import com.ztiany.session.utils.Utils;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.ztiany.session.Constants.LAST_ACCESS_TIME;

/**
 * 利用Cookie记住最后访问的时间
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.18 22:58
 */
public class ShowLastAccessTimeServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Utils.fixEncode(request, response);

        //获取cookie
        Cookie[] cookies = request.getCookies();
        String lastAccessTime = null;
        for (Cookie cookie : cookies) {
            if (LAST_ACCESS_TIME.equals(cookie.getName())) {
                lastAccessTime = cookie.getValue();
                break;
            }
        }
        if (Objects.isNull(lastAccessTime)) {
            response.getWriter().write("欢迎，您是第一次访问<br/>");
        } else {
            response.getWriter().write("欢迎，您上一次访问的时间为：" + lastAccessTime + "<br/>");
            //清除时间的超链接
            response.getWriter().write("<a href='" + request.getContextPath() + "/servlet/ClearLastAccessTimeServlet'>清除</a>");
        }

        Cookie cookie = new Cookie(LAST_ACCESS_TIME, new Date().toLocaleString());
        //设置cookie 的路径为应用路径
        cookie.setPath(request.getContextPath());
        cookie.setMaxAge(60 * 60);
        response.addCookie(cookie);
    }
}
