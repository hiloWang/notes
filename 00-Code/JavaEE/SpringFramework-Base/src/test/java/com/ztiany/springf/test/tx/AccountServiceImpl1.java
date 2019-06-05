package com.ztiany.springf.test.tx;

import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

/*使用TransactionTemplate*/
public class AccountServiceImpl1 implements AccountService {

    private AccountDao mAccountDao;
    private TransactionTemplate mTransactionTemplate;

    @Override
    public void transfer(final Integer from, final Integer to, final Double money) {
        mTransactionTemplate.execute(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus arg0) {
                //减钱
                mAccountDao.decreaseMoney(from, money);
                //模拟异常
                int i = 1 / 0;
                //加钱
                mAccountDao.increaseMoney(to, money);
            }
        });
    }

    public void setAccountDao(AccountDao accountDao) {
        mAccountDao = accountDao;
    }

    public void setTransactionTemplate(TransactionTemplate transactionTemplate) {
        mTransactionTemplate = transactionTemplate;
    }
}
