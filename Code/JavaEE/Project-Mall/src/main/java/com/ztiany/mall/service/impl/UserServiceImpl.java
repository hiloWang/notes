package com.ztiany.mall.service.impl;

import com.ztiany.mall.config.AppConfig;
import com.ztiany.mall.dao.UserDao;
import com.ztiany.mall.domain.User;
import com.ztiany.mall.service.UserService;
import com.ztiany.mall.utils.BeanFactory;
import com.ztiany.mall.utils.CommonsUtils;
import com.ztiany.mall.utils.MD5Utils;

import java.sql.SQLException;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.13 23:02
 */
public class UserServiceImpl implements UserService {

    @Override
    public User login(String username, String password) throws SQLException {
        UserDao userDao = BeanFactory.getBean(AppConfig.USER_DAO);
        return userDao.findUser(username, MD5Utils.md5(password));
    }

    @Override
    public User autoLogin(String username, String password) throws SQLException {
        UserDao userDao = BeanFactory.getBean(AppConfig.USER_DAO);
        return userDao.findUser(username, password);
    }

    @Override
    public boolean checkUserExist(String username) throws SQLException {
        UserDao userDao = BeanFactory.getBean(AppConfig.USER_DAO);
        return userDao.checkUserExist(username) != 0;
    }

    @Override
    public int register(User user) throws SQLException {
        UserDao userDao = BeanFactory.getBean(AppConfig.USER_DAO);
        User databaseUser = userDao.findUserByUsername(user.getUsername());
        if (databaseUser != null) {
            return AppConfig.USER_EXIST;
        } else {
            user.setUid(CommonsUtils.getUUID());
            user.setCode(CommonsUtils.getUUID());
            user.setPassword(MD5Utils.md5(user.getPassword()));
            user.setState(AppConfig.NO_ACTIVE);
            userDao.saveUser(user);
            return AppConfig.SUCCESS;
        }
    }

    @Override
    public int active(String activeCode) throws SQLException {
        UserDao userDao = BeanFactory.getBean(AppConfig.USER_DAO);
        User databaseUser = userDao.findUserByCode(activeCode);
        if (databaseUser != null) {
            userDao.updateUserActiveState(activeCode, AppConfig.ACTIVATED);
            return AppConfig.SUCCESS;
        } else {
            return AppConfig.ACTIVE_CODE_NOT_MATCH;
        }
    }

}
