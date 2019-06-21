package com.ztiany.register;

import com.ztiany.register.dao.UserDao;
import com.ztiany.register.dao.impl.UserDaoImpl;
import com.ztiany.register.domain.User;

import org.junit.Test;

import java.util.Date;
import java.util.ResourceBundle;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.22 21:59
 */
public class TestDao {

    @Test
    public void testSave() {
        UserDao userDao = new UserDaoImpl();
        User user = new User();
        user.setUsername("zhangxy");
        user.setPassword("123456");
        user.setEmail("zhangxy@163.com");
        user.setBirthday(new Date());
        userDao.save(user);
    }

    @Test
    public void testNewDao() {
        ResourceBundle rb = ResourceBundle.getBundle("dao");
        System.out.println(rb.getString("dao"));
    }
}
