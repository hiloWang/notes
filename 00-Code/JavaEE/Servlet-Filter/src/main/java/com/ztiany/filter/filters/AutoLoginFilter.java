package com.ztiany.filter.filters;

import com.ztiany.filter.utils.EncodeUtil;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 自动登录示例
 */
public class AutoLoginFilter implements Filter {

    private BusinessService mBusinessService = new BusinessService();

    public void init(FilterConfig filterConfig) {

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

        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");

        if (u == null) {
            /*
            自动登录，仅仅是示例，方敏感信息通过Cookie存放在客户端是非常不可取的，也是很危险的。

            登录时存Cookie：
            Cookie c = new Cookie("loginInfo", EncodeUtil.base64Encode(username)+"_"+EncodeUtil.md5(password));
            c.setPath(request.getContextPath());
            c.setMaxAge(Integer.MAX_VALUE);
            response.addCookie(c);
             */
            Cookie cs[] = request.getCookies();
            for (int i = 0; cs != null && i < cs.length; i++) {

                if ("loginInfo".equals(cs[i].getName())) {

                    //找到cookie
                    String value = cs[i].getValue();
                    String username = value.split("_")[0];//base64
                    String password = value.split("_")[1];//md5

                    //再次验证用户名和密码的正确性
                    User user = mBusinessService.findByUsername(EncodeUtil.base64Decode(username));

                    if (user != null) {
                        if (password.equals(EncodeUtil.md5(user.getPassword()))) {
                            //用户名和密码都正确
                            session.setAttribute("user", user);
                        }
                    }

                    break;
                }
            }
        }

        chain.doFilter(request, response);
    }

    public void destroy() {

    }

    //just for test
    private class BusinessService {
        User findByUsername(String name) {
            return null;
        }
    }

    //just for test
    @SuppressWarnings("all")
    private class User implements Serializable {
        private static final long serialVersionUID = 42L;

        public String getPassword() {
            return null;
        }

        public String getUsername() {
            return null;
        }
    }

}
