package me.ztiany.mybatis.test.basic;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.Date;

import me.ztiany.mybatis.pojo.User;
import me.ztiany.mybatis.test.basic.dao.UserDao;
import me.ztiany.mybatis.test.basic.dao.UserDaoImpl;

/**
 * MyBatis + 传统 DAO 模式：DAO编写繁复
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.26 22:58
 */
public class MyBatisDaoTest {

    private UserDao mUserDao;

    @Before
    public void before() throws Exception {
        //加载核心配置文件
        InputStream in = Resources.getResourceAsStream("sqlMapConfig_Basic.xml");
        //创建SqlSessionFactory
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
        mUserDao = new UserDaoImpl(sqlSessionFactory);
    }

    @Test
    public void testDao() {
        User user = mUserDao.selectUserById(10);
        System.out.println(user);
    }

    @Test
    public void testInsert() {
        //执行Sql语句
        User user = new User();
        user.setUsername("谭咏麟");
        user.setBirthday(new Date());
        user.setAddress("香港");
        user.setSex("男");

        //返回插入后，影响的行数
        int insert = mUserDao.insertUser(user);

        System.out.println("insert: " + insert);
        System.out.println("last insert user: " + user.getId());
    }

}
