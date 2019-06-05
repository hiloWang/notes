package com.ztiany.mysql.hibernate.api;

import com.ztiany.mysql.hibernate.Customer;
import com.ztiany.mysql.hibernate.HibernateUtils;

import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * ORM数据库框架Hibernate简单示例
 */
public class CRUDDemo {

    public static void main(String... args) {
        CRUDDemo crudDemo = new CRUDDemo();
        //crudDemo.add();
        crudDemo.find();
        //crudDemo.update();
        //crudDemo.find();
        //crudDemo.del();
    }

    public void add() {
        Customer c = new Customer();
        c.setCust_name("杀生丸");
        c.setCust_level("VIP");

        Session s = HibernateUtils.getCurrentSession();

        Transaction tx = s.beginTransaction();//开启事务
        s.save(c);
        tx.commit();

        s.close();
    }

    public void find() {
        Session session = HibernateUtils.getCurrentSession();
        Transaction transaction = session.beginTransaction();
        Customer c = session.get(Customer.class, 1L);
        System.out.println(c);
        transaction.commit();
        session.close();
    }

    public void update() {
        Session s = HibernateUtils.getCurrentSession();

        Transaction tx = s.beginTransaction();//开启事务

        Customer c = s.get(Customer.class, 1);
        c.setCust_name("杀生丸");
        s.update(c);

        tx.commit();

        s.close();
    }


    public void del() {
        Session s = HibernateUtils.getCurrentSession();

        Transaction tx = s.beginTransaction();//开启事务

        Customer c = s.get(Customer.class, 1);
        s.delete(c);

        tx.commit();
        //tx.rollback();

        s.close();
    }
}
