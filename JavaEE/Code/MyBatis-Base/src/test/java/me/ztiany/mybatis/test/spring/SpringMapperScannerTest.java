package me.ztiany.mybatis.test.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import javax.annotation.Resource;

import me.ztiany.mybatis.pojo.Orders;
import me.ztiany.mybatis.test.basic.mapper.OrderMapper;
import me.ztiany.mybatis.test.basic.mapper.UserMapper;

/**
 * 测试Mapper自动扫描
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.27 18:44
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring_mapper_scanner.xml")
public class SpringMapperScannerTest {

    @Resource
    private UserMapper mUserMapper;
    @Resource
    private OrderMapper mOrderMapper;

    @Test
    public void testMapper() {
        Integer countUser = mUserMapper.countUser();
        System.out.println("countUser: " + countUser);
        List<Orders> ordersList = mOrderMapper.selectOrders();
        System.out.println("ordersList: " + ordersList);
    }

}
