package com.ztiany.springf.test.jdbc;


import com.mchange.v2.c3p0.ComboPooledDataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


//演示JDBC模板
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:jdbc.xml")
public class JDBCTest {

    @Resource(name = "userDao")
    private UserDao mUserDao;

    @Test
    public void testJdbcTemplate() throws Exception {
        //准备连接池
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        dataSource.setDriverClass("com.mysql.jdbc.Driver");
        dataSource.setJdbcUrl("jdbc:mysql:///sqlbase");
        dataSource.setUser("root");
        dataSource.setPassword("201314");

        //1 创建JDBC模板对象
        JdbcTemplate jdbcTemplate = new JdbcTemplate();
        jdbcTemplate.setDataSource(dataSource);

        //2 书写sql,并执行
        String sql = "INSERT INTO springf_test_user VALUES(NULL,'rose') ";
        jdbcTemplate.update(sql);
    }

    @Test
    public void fun2() {
        User u = new User();
        u.setName("tom");
        mUserDao.save(u);
    }

    @Test
    public void fun3() {
        User u = new User();
        u.setId(2);
        u.setName("jack");
        mUserDao.update(u);
    }

    @Test
    public void fun4() {
        mUserDao.delete(2);
    }

    @Test
    public void fun5() {
        System.out.println(mUserDao.getTotalCount());
    }

    @Test
    public void fun6() {
        System.out.println(mUserDao.getById(1));
    }

    @Test
    public void fun7() {
        System.out.println(mUserDao.getAll());
    }

}
