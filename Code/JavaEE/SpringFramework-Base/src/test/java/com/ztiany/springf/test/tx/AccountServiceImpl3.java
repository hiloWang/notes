package com.ztiany.springf.test.tx;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/*注解是使用事务模板的方式(针对类中所有方法)*/
@Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED, readOnly = true)
public class AccountServiceImpl3 implements AccountService {

    private AccountDao mAccountDao;

    @Override
    /*仅针对方法，优先级高*/
    @Transactional(isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    public void transfer(final Integer from, final Integer to, final Double money) {
        //减钱
        mAccountDao.decreaseMoney(from, money);
        //模拟异常
        int i = 1 / 0;
        //加钱
        mAccountDao.increaseMoney(to, money);
    }

    public void setAccountDao(AccountDao accountDao) {
        mAccountDao = accountDao;
    }

}
