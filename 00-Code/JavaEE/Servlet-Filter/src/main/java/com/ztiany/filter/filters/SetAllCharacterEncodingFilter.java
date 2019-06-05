package com.ztiany.filter.filters;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class SetAllCharacterEncodingFilter implements Filter {

    private FilterConfig filterConfig;
    private ServletContext mServletContext;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        mServletContext = filterConfig.getServletContext();
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

        //hwo to get runtime information
        System.out.println(mServletContext.getMajorVersion());//服务器支持的Servlet主版本号
        System.out.println(mServletContext.getMinorVersion());//服务器支持的Servlet次版本号
        Enumeration attributeNames = mServletContext.getAttributeNames();


        while (attributeNames.hasMoreElements()) {
            Object name = attributeNames.nextElement();
            System.out.println(name + " = " + mServletContext.getAttribute((String) name));
        }

        String encoding = filterConfig.getInitParameter("encoding");

        if (encoding == null) {
            encoding = "UTF-8";
        }

        request.setCharacterEncoding(encoding);//POST方式，同时也可以给后面过滤器或Servlet使用
        response.setCharacterEncoding(encoding);//给后面过滤器或Servlet使用
        response.setContentType("text/html;charset=" + encoding);//输出字符流使用的编码，告知客户端用什么编码解码

        CodeFixHttpServletRequest codeFixHttpServletRequest = new CodeFixHttpServletRequest(request);

        chain.doFilter(codeFixHttpServletRequest, response);
    }

    public void destroy() {

    }

    private class CodeFixHttpServletRequest extends HttpServletRequestWrapper {

        CodeFixHttpServletRequest(HttpServletRequest request) {
            super(request);
        }

        public String getParameter(String name) {
            String value = super.getParameter(name);
            if (value == null) {
                return null;
            }

            //只管get方式
            String method = super.getMethod();

            if ("get".equalsIgnoreCase(method)) {
                value = fixGetEncoding(value);
            }
            return value;
        }


        private String fixGetEncoding(String value) {

            try {
                //for tomcat 6/7
                value = new String(value.getBytes("ISO-8859-1"), super.getCharacterEncoding());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return value;
        }

    }


}
