package com.ztiany.springf.test.basic;

import com.ztiany.springf.test.basic.domain.CollectionBean;
import com.ztiany.springf.test.basic.domain.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Spring 手动加载配置文件，手动从容器中获取对象。
 */
public class ConfigTest {

    private ApplicationContext applicationContext;

    @Before
    public void init() {
        //1 创建容器对象
        applicationContext = new ClassPathXmlApplicationContext("basic_config.xml");
    }

    @Test
    public void testApplicationContext() {
        //2 向容器"要"user对象
        User user = (User) applicationContext.getBean("user1");
        assertNotNull(user);
    }

    @Test
    public void testBeanFactory() {
        //1 创建容器对象
        XmlBeanFactory factory = new XmlBeanFactory(new ClassPathResource("basic_config.xml"));
        //2 向容器"要"user对象
        User user = (User) factory.getBean("user1");
        assertNotNull(user);
    }

    @Test
    public void testScope() {
        //No qualifying bean of type 'com.ztiany.springf.test.basic.User' available: expected single matching bean but found 8: user1,user2,user3,user4,user5,user6,user7,user8
        //当存在多个相同类型的对象在容器中时，不能使用下面方法
        //User user = applicationContext.getBean(User.class);
        //System.out.println(user);

        User user1First = (User) applicationContext.getBean("user1");
        User user1Second = (User) applicationContext.getBean("user1");
        assertNotEquals(user1First, user1Second);
        System.out.println(user1First);
        System.out.println(user1Second);
    }

    @Test
    public void testLifecycle() {
        User user2 = (User) applicationContext.getBean("user2");
        assertNotNull(user2);
    }

    @Test
    public void testInjection() {
        CollectionBean collection_bean = (CollectionBean) applicationContext.getBean("collection_bean");
        System.out.println(collection_bean);
        assertNotNull(collection_bean);
    }

    @After
    public void destroy() {
        ((ClassPathXmlApplicationContext) applicationContext).close();
    }

}