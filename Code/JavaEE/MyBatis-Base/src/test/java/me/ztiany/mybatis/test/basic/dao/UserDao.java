package me.ztiany.mybatis.test.basic.dao;


import java.util.List;

import me.ztiany.mybatis.pojo.User;

public interface UserDao {

    //通过用户ID查询一个用户
    User selectUserById(Integer id);

    //通过用户名称模糊查询
    List<User> selectUserByUsername(Integer id);

    //插入用户
    Integer insertUser(User user);

}
