package com.ztiany.mysql.aop.service;


import com.ztiany.mysql.aop.dao.AccountDao;
import com.ztiany.mysql.aop.dao.AccountDaoImpl;
import com.ztiany.mysql.aop.domain.Account;

/**
 * java.sql.*专属DAO层，纯粹的业务处理
 */
public class BusinessServiceImpl implements BusinessService {

    private AccountDao dao = new AccountDaoImpl();

    public void transfer(String sourceAccountName, String targetAccountName, float money) {
        Account sAccount = dao.findAccountByName(sourceAccountName);
        Account tAccount = dao.findAccountByName(targetAccountName);
        sAccount.setMoney(sAccount.getMoney() - money);
        tAccount.setMoney(tAccount.getMoney() + money);
        dao.updateAccount(sAccount);
        //int i = 1 / 0;//模拟事务中断
        dao.updateAccount(tAccount);
    }

}
