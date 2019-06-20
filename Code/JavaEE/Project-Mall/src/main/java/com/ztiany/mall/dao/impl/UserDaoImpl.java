package com.ztiany.mall.dao.impl;

import com.ztiany.mall.dao.UserDao;
import com.ztiany.mall.domain.User;
import com.ztiany.mall.utils.TransactionManager;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.17 22:27
 */
public class UserDaoImpl implements UserDao {

    @Override
    public User findUser(String username, String password) throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        return queryRunner.query(TransactionManager.getConnection(), "select * from user where username = ? and password = ?", new BeanHandler<>(User.class), username, password);
    }

    //校验用户名是否存在
    public Long checkUserExist(String username) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "select count(*) from user where username=?";
        return runner.query(TransactionManager.getConnection(), sql, new ScalarHandler<Long>(), username);
    }

    @Override
    public User findUserByUsername(String username) throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        return queryRunner.query(TransactionManager.getConnection(), "select * from user where username = ?", new BeanHandler<>(User.class), username);
    }

    @Override
    public void saveUser(User user) throws SQLException {
        String sql = "insert into user values(?,?,?,?,?,?,?,?,?,?)";
        QueryRunner queryRunner = new QueryRunner();
        queryRunner.update(TransactionManager.getConnection(), sql,
                user.getUid(),
                user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getEmail(),
                user.getTelephone(),
                user.getBirthday(),
                user.getSex(),
                user.getState(),
                user.getCode());
    }

    @Override
    public User findUserByCode(String activeCode) throws SQLException {
        QueryRunner queryRunner = new QueryRunner();
        return queryRunner.query(TransactionManager.getConnection(), "select * from user where code = ?", new BeanHandler<>(User.class), activeCode);
    }

    @Override
    public void updateUserActiveState(String activeCode, int state) throws SQLException {
        QueryRunner runner = new QueryRunner();
        String sql = "update user set state=? where code=?";
        runner.update(TransactionManager.getConnection(), sql, state, activeCode);
    }

}
