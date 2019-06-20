package me.ztiany.mybatis.test.spring;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import me.ztiany.mybatis.pojo.User;
import me.ztiany.mybatis.test.basic.mapper.UserMapper;

/**
 * 测试Mapper配置
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.27 18:44
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring_mapper.xml")
public class SpringMapperTest {

    @Resource(name = "userMapper")
    private UserMapper mUserMapper;

    @Test
    public void testMapper() {
        Integer countUser = mUserMapper.countUser();
        System.out.println("countUser: " + countUser);
    }

    /**
     * 内部使用的是org.mybatis.spring.SqlSessionTemplate，会自动提交事务
     */
    @Test
    public void testInsertUser() {
        //执行Sql语句
        User user = new User();
        user.setUsername("古天乐");
        user.setBirthday(new Date());
        user.setAddress("香港");
        user.setSex("男");

        //返回插入后，影响的行数
        int insert = mUserMapper.insertUser(user);

        System.out.println("insert: " + insert);
        System.out.println("last insert user: " + user.getId());
    }


    /**
     * 在单表批量插入时，使用MyBatis的事务管理
     */
    @Test
    public void testBatchInsertUser() {
        List<User> userList = new ArrayList<>();

        User user1 = new User();
        user1.setUsername("小李");
        user1.setBirthday(new Date());
        user1.setAddress("深圳");
        user1.setSex("男");

        User user2 = new User();
        //user1.setUsername("小李"); //模拟插入失败
        user2.setBirthday(new Date());
        user2.setAddress("广东");
        user2.setSex("男");

        userList.add(user1);
        userList.add(user2);

        //返回插入后，影响的行数
        int insert = mUserMapper.batchInsert(userList);

        System.out.println("insert: " + insert);
        System.out.println("last insert user1: " + user1.getId());
    }

}
