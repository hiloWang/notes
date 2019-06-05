package com.ztiany.filter.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 设置静态资源的缓存时间
 */
public class StaticResourceNeedCacheFilter implements Filter {

    private FilterConfig filterConfig;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request;
        HttpServletResponse response;

        try {
            request = (HttpServletRequest) req;
            response = (HttpServletResponse) resp;
        } catch (Exception e) {
            throw new RuntimeException("non http request");
        }

        long time = 0;//毫秒

        //获取用户访问的资源的类型：通过uri资源地址的后缀
        String uri = request.getRequestURI();
        String extendName = uri.substring(uri.lastIndexOf(".") + 1);
        if ("html".equals(extendName)) {
            String hour = filterConfig.getInitParameter("html");
            time = Long.parseLong(hour) * 60 * 60 * 1000;
        }
        if ("js".equals(extendName)) {
            String hour = filterConfig.getInitParameter("js");
            time = Long.parseLong(hour) * 60 * 60 * 1000;
        }
        if ("css".equals(extendName)) {
            String hour = filterConfig.getInitParameter("css");
            time = Long.parseLong(hour) * 60 * 60 * 1000;
        }

        response.setDateHeader("Expires", System.currentTimeMillis() + time);

        chain.doFilter(request, response);
    }

    public void destroy() {

    }

}
