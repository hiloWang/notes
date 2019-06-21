package com.ztiany.customer.utils;

import com.ztiany.customer.dao.CustomerDao;

import java.util.ResourceBundle;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.26 21:59
 */
public class DaoFactory {

    private DaoFactory() {
    }

    public static CustomerDao newUserDao() {
        try {
            ResourceBundle rb = ResourceBundle.getBundle("dao");
            return (CustomerDao) Class.forName(rb.getString("dao")).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("创建UserDao实例对象失败");
        }
    }

}
