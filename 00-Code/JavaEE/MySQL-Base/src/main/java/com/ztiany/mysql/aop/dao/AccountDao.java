package com.ztiany.mysql.aop.dao;


import com.ztiany.mysql.aop.domain.Account;

public interface AccountDao {

    Account findAccountByName(String name);

    void updateAccount(Account account);

}
