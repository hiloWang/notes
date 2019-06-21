package com.ztiany.mall.web.servlet;

import com.ztiany.mall.config.AppConfig;
import com.ztiany.mall.domain.Category;
import com.ztiany.mall.domain.Product;
import com.ztiany.mall.service.ProductService;
import com.ztiany.mall.utils.BeanFactory;
import com.ztiany.mall.utils.JedisPoolUtils;
import com.ztiany.mall.utils.JsonUtils;
import com.ztiany.mall.web.bean.History;
import com.ztiany.mall.web.bean.Page;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import redis.clients.jedis.Jedis;

import static com.ztiany.mall.config.AppConfig.CATEGORY_LIST_KEY;
import static com.ztiany.mall.config.AppConfig.COOKIE_PRODUCT_VIEW_HISTORY;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.16 23:44
 */
@WebServlet(urlPatterns = "/product")
@SuppressWarnings("unused")
public class ProductServlet extends AbsBaseServlet {


    public void indexProduct(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
        ProductService service = BeanFactory.getProductService();
        System.out.println(request.getContextPath());
        //准备热门商品---List<Product>
        List<Product> hotProductList = service.findHotProductList();
        //准备最新商品---List<Product>
        List<Product> newProductList = service.findNewProductList();

        System.out.println(hotProductList);
        System.out.println(newProductList);

        //准备商品数据
        request.setAttribute(AppConfig.HOT_PRODUCT_LIST, hotProductList);
        request.setAttribute(AppConfig.NEW_PRODUCT_LIST, newProductList);

        //转发请求
        request.getRequestDispatcher("/index.jsp").forward(request, response);
    }


    public void productInfo(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
        //获得当前页
        String currentPage = request.getParameter("currentPage");
        //获得商品类别
        String cid = request.getParameter("cid");
        //获得要查询的商品的pid
        String pid = request.getParameter("pid");

        ProductService service = BeanFactory.getProductService();
        Product product = service.findProductByPid(pid);

        request.setAttribute("product", product);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("cid", cid);

        //历史记录处理
        String pidJson = "";
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_PRODUCT_VIEW_HISTORY.equals(cookie.getName())) {
                    pidJson = cookie.getValue();
                }
            }
        }
        History history = new History(URLDecoder.decode(pidJson, "UTF-8"));
        history.add(pid);
        Cookie pidJsonCookie = new Cookie(COOKIE_PRODUCT_VIEW_HISTORY, URLEncoder.encode(history.getHistoryJson(), "UTF-8"));
        response.addCookie(pidJsonCookie);

        request.getRequestDispatcher("/product_info.jsp").forward(request, response);
    }

    public void productList(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException, SQLException {
        ProductService service = BeanFactory.getProductService();

        String cid = request.getParameter("cid");
        String currentPageStr = request.getParameter("currentPage");

        Page pageBean = service.findProductListByCid(cid, currentPageStr);
        pageBean.setUrl("/product?action=productList&cid=" + cid);

        request.setAttribute("page", pageBean);
        request.setAttribute("cid", cid);

        //历史记录处理
        List<Product> historyProductList = new ArrayList<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_PRODUCT_VIEW_HISTORY.equals(cookie.getName())) {
                    String pidJson = cookie.getValue();//3-2-1
                    History history = new History(URLDecoder.decode(pidJson, "UTF-8"));
                    for (String pid : history.getHistoryList()) {
                        Product pro = service.findProductByPid(pid);
                        historyProductList.add(pro);
                    }
                }
            }
        }
        request.setAttribute("historyProductList", historyProductList);


        request.getRequestDispatcher("/product_list.jsp").forward(request, response);
    }

    public void categoryList(HttpServletRequest request, HttpServletResponse response) throws
            IOException, SQLException {
        ProductService service = BeanFactory.getProductService();
        //先从缓存中查询categoryList
        //如果有直接使用
        //如果没有在从数据库中查询，存到缓存中

        //获得jedis对象 连接redis数据库
        Jedis jedis = JedisPoolUtils.getJedis();
        String categoryListJson = jedis.get(CATEGORY_LIST_KEY);

        //判断categoryListJson是否为空
        if (categoryListJson == null) {
            //准备分类数据
            List<Category> categoryList = service.findAllCategory();
            categoryListJson = JsonUtils.toJson(categoryList);
            jedis.set(CATEGORY_LIST_KEY, categoryListJson);
        }

        response.getWriter().write(categoryListJson);
    }


}
