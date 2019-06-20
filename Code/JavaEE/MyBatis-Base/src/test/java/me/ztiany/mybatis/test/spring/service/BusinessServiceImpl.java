package me.ztiany.mybatis.test.spring.service;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import me.ztiany.mybatis.pojo.User;
import me.ztiany.mybatis.test.basic.mapper.UserMapper;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.27 22:20
 */
public class BusinessServiceImpl implements BusinessService {

    @Resource
    private UserMapper mUserMapper;

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void insertUser(User user) {
        mUserMapper.insertUser(user);
        int a = 1 / 0;
        System.out.println(a);
    }

}
