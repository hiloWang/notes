package com.ztiany.mall.dao;

import com.ztiany.mall.domain.Category;
import com.ztiany.mall.domain.Product;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.19 9:37
 */
public interface ProductDao {

    List<Product> findProductListByIsHot(int hotFlag) throws SQLException;

    List<Product> findNewProductList() throws SQLException;

    Product findProductByPid(String pid) throws SQLException;

    List<Product> findProductByPage(String cid, int index, int currentCount) throws SQLException;

    int getCount(String cid) throws SQLException;

    List<Category> findAllCategory() throws SQLException;
}
