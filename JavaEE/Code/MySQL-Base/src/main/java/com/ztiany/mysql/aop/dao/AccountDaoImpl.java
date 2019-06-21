package com.ztiany.mysql.aop.dao;

import com.ztiany.mysql.aop.domain.Account;
import com.ztiany.mysql.aop.tools.TransactionManager;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.SQLException;


/**
 * DAO只做CRUD，DAO不能处理业务逻辑（需要事务控制的，都是业务要求）
 */
public class AccountDaoImpl implements AccountDao {

    private QueryRunner qr = new QueryRunner();

    public Account findAccountByName(String name) {
        try {
            return qr.query(TransactionManager.getConnection(), "select * from aop_account where name=?", new BeanHandler<Account>(Account.class), name);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateAccount(Account account) {
        try {
            qr.update(TransactionManager.getConnection(), "update aop_account set money=?,name=? where id=?", account.getMoney(), account.getName(), account.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}
