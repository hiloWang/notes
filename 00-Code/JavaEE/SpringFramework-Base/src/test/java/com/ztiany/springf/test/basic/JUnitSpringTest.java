package com.ztiany.springf.test.basic;

import com.ztiany.springf.test.basic.domain.User;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.assertNotNull;

/**
 * 使用 Spring 提供的测试框架，简化Spring 单元测试
 */
//创建容器
@RunWith(SpringJUnit4ClassRunner.class)
//指定创建容器时使用哪个配置文件
@ContextConfiguration(locations = "classpath:basic_config.xml")
public class JUnitSpringTest {

    //将名为user的对象注入到mUser变量中
    @Resource(name = "user1")
    private User mUser;

    @Test
    public void fun1() {
        System.out.println(mUser);
        assertNotNull(mUser);
    }

}
