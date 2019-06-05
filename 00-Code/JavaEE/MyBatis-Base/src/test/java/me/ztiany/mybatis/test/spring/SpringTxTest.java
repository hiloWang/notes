package me.ztiany.mybatis.test.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

import javax.annotation.Resource;

import me.ztiany.mybatis.pojo.User;
import me.ztiany.mybatis.test.spring.service.BusinessService;

/**
 * 测试使用Spring提供的事务来管理MyBatis的数据库操作
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.27 22:48
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring_tx_manager.xml")
public class SpringTxTest {

    @Resource(name = "businessService")
    private BusinessService mBusinessService;

    @Test
    public void testSpringTx() {
        User user = new User();
        user.setUsername("香香2");
        user.setBirthday(new Date());
        user.setAddress("深圳");
        user.setSex("女");
        mBusinessService.insertUser(user);
    }


}
