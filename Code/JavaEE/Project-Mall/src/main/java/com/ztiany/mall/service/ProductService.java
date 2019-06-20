package com.ztiany.mall.service;

import com.ztiany.mall.domain.Category;
import com.ztiany.mall.domain.Product;
import com.ztiany.mall.web.bean.Page;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.19 9:35
 */
public interface ProductService {

    List<Product> findHotProductList() throws SQLException;

    List<Product> findNewProductList() throws SQLException;

    List<Category> findAllCategory() throws SQLException;

    Page findProductListByCid(String cid, String currentPageParams) throws SQLException;

    Product findProductByPid(String pid) throws SQLException;
}
