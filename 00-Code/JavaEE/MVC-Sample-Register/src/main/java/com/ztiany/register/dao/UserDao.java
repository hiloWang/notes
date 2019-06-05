package com.ztiany.register.dao;

import com.ztiany.register.domain.User;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.22 18:03
 */
public interface UserDao {

    void save(User user);

    User findUser(String name);

    User findUser(String name, String password);

}
