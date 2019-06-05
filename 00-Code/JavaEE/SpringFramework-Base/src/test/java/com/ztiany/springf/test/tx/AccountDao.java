package com.ztiany.springf.test.tx;


public interface AccountDao {

    //加钱
    void increaseMoney(Integer id, Double money);

    //减钱
    void decreaseMoney(Integer id, Double money);
}
