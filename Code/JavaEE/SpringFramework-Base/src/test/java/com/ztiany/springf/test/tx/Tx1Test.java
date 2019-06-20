package com.ztiany.springf.test.tx;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * 测试使用 TransactionTemplate
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:tx1.xml")
public class Tx1Test {

    @Resource(name = "accountService")
    private AccountService as;

    @Test
    public void fun1() {
        as.transfer(1, 2, 100D);
    }
}
