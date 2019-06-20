package com.ztiany.filter.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class HtmlFilter implements Filter {

    public void init(FilterConfig filterConfig) {

    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        System.out.println("HtmlFilter.doFilter "+chain);

        HttpServletRequest request;
        HttpServletResponse response;

        try {
            request = (HttpServletRequest) req;
            response = (HttpServletResponse) resp;
        } catch (Exception e) {
            throw new RuntimeException("non http request");
        }

        HtmlHttpServletRequest htmlHttpServletRequest = new HtmlHttpServletRequest(request);
        chain.doFilter(htmlHttpServletRequest, response);

    }

    public void destroy() {

    }

    private class HtmlHttpServletRequest extends HttpServletRequestWrapper {

        HtmlHttpServletRequest(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getParameter(String name) {
            String value = super.getParameter(name);
            if (value == null) {
                return null;
            }
            value = htmlFilter(value);
            return value;
        }

        private String htmlFilter(String message) {
            if (message == null) {
                return (null);
            }

            char content[] = new char[message.length()];

            message.getChars(0, message.length(), content, 0);

            StringBuilder result = new StringBuilder(content.length + 50);

            for (char aContent : content) {
                switch (aContent) {
                    case '<':
                        result.append("&lt;");
                        break;
                    case '>':
                        result.append("&gt;");
                        break;
                    case '&':
                        result.append("&amp;");
                        break;
                    case '"':
                        result.append("&quot;");
                        break;
                    default:
                        result.append(aContent);
                }
            }
            return (result.toString());
        }
    }

}
