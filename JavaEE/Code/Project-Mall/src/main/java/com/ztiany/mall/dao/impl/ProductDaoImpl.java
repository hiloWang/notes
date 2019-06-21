package com.ztiany.mall.dao.impl;

import com.ztiany.mall.dao.ProductDao;
import com.ztiany.mall.domain.Category;
import com.ztiany.mall.domain.Product;
import com.ztiany.mall.utils.TransactionManager;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.19 9:37
 */
public class ProductDaoImpl implements ProductDao {

    @Override
    public List<Product> findProductListByIsHot(int hotFlag) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from product where is_hot=? limit ?,?";
        return runner.query(TransactionManager.getConnection(), sql, new BeanListHandler<>(Product.class), hotFlag, 0, 9);
    }

    @Override
    public List<Product> findNewProductList() throws SQLException {
        QueryRunner runner = new QueryRunner();
        //找最新商品，即对日期进行排序
        String sql = "select * from product order by pdate desc limit ?,?";
        return runner.query(TransactionManager.getConnection(), sql, new BeanListHandler<>(Product.class), 0, 9);
    }

    @Override
    public List<Category> findAllCategory() throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from category";
        return runner.query(TransactionManager.getConnection(), sql, new BeanListHandler<>(Category.class));
    }

    @Override
    public int getCount(String cid) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select count(*) from product where cid=?";
        Long query = runner.query(TransactionManager.getConnection(), sql, new ScalarHandler<Long>(), cid);
        return query.intValue();
    }

    @Override
    public List<Product> findProductByPage(String cid, int index, int currentCount) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from product where cid=? limit ?,?";
        return runner.query(TransactionManager.getConnection(), sql, new BeanListHandler<>(Product.class), cid, index, currentCount);
    }

    @Override
    public Product findProductByPid(String pid) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select * from product where pid=?";
        return runner.query(TransactionManager.getConnection(), sql, new BeanHandler<>(Product.class), pid);
    }


}
