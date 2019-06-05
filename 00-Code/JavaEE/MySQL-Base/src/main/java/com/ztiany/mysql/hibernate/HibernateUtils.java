package com.ztiany.mysql.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtils {

    // 会话工厂，以单例方式管理
    private static SessionFactory sessionFactory;

    // 以单例方式管理sessionFactory
    static {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (HibernateException e) {
            e.printStackTrace();
            throw new HibernateException("初始化会话工厂失败！");
        }
    }

    //得到一个单例的会话工厂
    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    //获取一个新session
    public static Session openSession() {
        return sessionFactory.openSession();
    }

    public static Session getCurrentSession() throws HibernateException {
        return sessionFactory.getCurrentSession();
    }

    public static void closeSession() throws HibernateException {
        sessionFactory.getCurrentSession().close();
    }


}
