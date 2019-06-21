package me.ztiany.mybatis.test.spring.dao;

import me.ztiany.mybatis.pojo.User;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.27 18:31
 */
public interface UserDao {

    int countUser();

    int insertUser(User user);

}
