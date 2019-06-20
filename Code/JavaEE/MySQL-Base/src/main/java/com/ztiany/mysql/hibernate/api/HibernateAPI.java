package com.ztiany.mysql.hibernate.api;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * HibernateAPI
 */
public class HibernateAPI {

    /**
     * Configuration功能： 配置加载类.用于加载主配置，orm元数据加载。
     */
    public void configuration() {
        //1 创建,调用空参构造
        Configuration conf = new Configuration();
        //2 读取指定主配置文件 => 空参加载方法,加载src下的hibernate.cfg.xml文件
        conf.configure();

        //3 读取指定orm元数据(扩展),如果主配置中已经引入映射配置.不需要手动加载
        //conf.addResource(resourceName);
        //conf.addClass(persistentClass);

        //4 根据配置信息,创建 SessionFactory对象
        SessionFactory sf = conf.buildSessionFactory();
    }

    /**
     * SessionFactory功能: 用于创建操作数据库核心对象session对象的工厂。注意：
     * <p>
     * 1. sessionFactory 负责保存和使用所有配置信息.消耗内存资源非常大.
     * <p>
     * 2.sessionFactory属于线程安全的对象设计.
     * <p>
     * 结论: 保证在web项目中,只创建一个sessionFactory.
     */
    public void sessionFactory() {
        //1 创建,调用空参构造
        Configuration conf = new Configuration();
        //2 读取指定主配置文件 => 空参加载方法,加载src下的hibernate.cfg.xml文件
        conf.configure();

        //3 读取指定orm元数据(扩展),如果主配置中已经引入映射配置.不需要手动加载
        //conf.addResource(resourceName);
        //conf.addClass(persistentClass);

        //4 根据配置信息,创建 SessionFactory对象
        SessionFactory sf = conf.buildSessionFactory();

        //5 获得session
        //打开一个新的session对象
        sf.openSession();
        //获得一个与线程绑定的session对象
        sf.getCurrentSession();
    }

}
