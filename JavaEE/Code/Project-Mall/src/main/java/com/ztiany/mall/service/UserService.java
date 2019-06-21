package com.ztiany.mall.service;

import com.ztiany.mall.domain.User;

import java.sql.SQLException;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.13 21:54
 */
public interface UserService {

    User login(String username, String password) throws SQLException;

    User autoLogin(String username, String password) throws SQLException;

    boolean checkUserExist(String username) throws SQLException;

    int register(User user) throws SQLException;

    int active(String activeCode) throws SQLException;

}
