package com.ztiany.mall.dao;

import com.ztiany.mall.domain.User;

import java.sql.SQLException;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.13 21:55
 */
public interface UserDao {

    User findUser(String username, String password) throws SQLException;

    Long checkUserExist(String username) throws SQLException;

    User findUserByUsername(String username) throws SQLException;

    void saveUser(User user) throws SQLException;

    User findUserByCode(String activeCode) throws SQLException;

    void updateUserActiveState(String activeCode, int state)throws SQLException;
}
