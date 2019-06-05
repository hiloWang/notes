package me.ztiany.mybatis.test.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import me.ztiany.mybatis.test.spring.dao.UserDao;

/**
 * 演示以下Dao开发模式
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.27 18:31
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring_dao.xml")
public class SpringDaoTest {

    @Resource(name = "userDao")
    private UserDao mUserDao;

    @Test
    public void testDao() {
        int countUser = mUserDao.countUser();
        System.out.println("countUser: " + countUser);
    }

}
