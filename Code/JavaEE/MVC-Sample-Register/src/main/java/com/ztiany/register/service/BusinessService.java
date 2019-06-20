package com.ztiany.register.service;


import com.ztiany.register.domain.User;
import com.ztiany.register.exception.UserExistException;

public interface BusinessService {

    /**
     * 完成用户信息注册
     *
     * @param user 用户
     * @throws UserExistException 用户名已经存在了
     */
    void register(User user) throws UserExistException;

    /**
     * 完成用户的登录
     *
     * @param username 用户名
     * @param password 密码
     * @return 错误的用户名或密码，返回null
     */
    User login(String username, String password);
}
