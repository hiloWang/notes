package com.ztiany.mall.service.impl;

import com.ztiany.mall.config.AppConfig;
import com.ztiany.mall.dao.ProductDao;
import com.ztiany.mall.domain.Category;
import com.ztiany.mall.domain.Product;
import com.ztiany.mall.service.ProductService;
import com.ztiany.mall.utils.BeanFactory;
import com.ztiany.mall.utils.WebBeanUtils;
import com.ztiany.mall.web.bean.Page;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.19 9:36
 */
public class ProductServiceImpl implements ProductService {

    @Override
    public List<Product> findHotProductList() throws SQLException {
        ProductDao dao = BeanFactory.getBean(AppConfig.PRODUCT_DAO);
        return dao.findProductListByIsHot(AppConfig.HOT_PRODUCT);
    }

    @Override
    public List<Product> findNewProductList() throws SQLException {
        ProductDao dao = BeanFactory.getBean(AppConfig.PRODUCT_DAO);
        return dao.findNewProductList();
    }

    public List<Category> findAllCategory() throws SQLException {
        ProductDao dao = BeanFactory.getBean(AppConfig.PRODUCT_DAO);
        return dao.findAllCategory();
    }

    public Page findProductListByCid(String cid, String currentPageParams) throws SQLException {

        int currentPage = WebBeanUtils.parsePage(currentPageParams);

        ProductDao dao = BeanFactory.getBean(AppConfig.PRODUCT_DAO);

        //总记录条数
        int totalCount = dao.getCount(cid);

        //封装page
        Page<Product> pageBean = new Page<>(currentPage, totalCount, AppConfig.PRODUCT_PAGE_SIZE);
        List<Product> productList = dao.findProductByPage(cid, pageBean.getStartIndex(), pageBean.getPageSize());
        pageBean.setRecord(productList);

        return pageBean;
    }

    public Product findProductByPid(String pid) throws SQLException {
        ProductDao dao = BeanFactory.getBean(AppConfig.PRODUCT_DAO);
        return dao.findProductByPid(pid);
    }

}
