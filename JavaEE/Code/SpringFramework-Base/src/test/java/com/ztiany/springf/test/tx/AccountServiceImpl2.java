package com.ztiany.springf.test.tx;

/*由xml配置事务，这里不需要任何配置*/
public class AccountServiceImpl2 implements AccountService {

    private AccountDao mAccountDao;

    @Override
    /*仅针对方法，优先级高*/
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
