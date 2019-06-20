package com.ztiany.mysql.hibernate.query;

import com.ztiany.mysql.hibernate.Customer;
import com.ztiany.mysql.hibernate.HibernateUtils;

import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Arrays;
import java.util.List;

/**
 * 测试原生SQL查询
 */
public class OriginSQL {


    //基本查询
    public void fun1() {
        //1 获得session
        Session session = HibernateUtils.openSession();
        //2 控制事务
        Transaction tx = session.beginTransaction();
        //3执行操作
        //-------------------------------------------
        //1 书写sql语句
        String sql = "SELECT * FROM cst_customer";

        //2 创建sql查询对象
        SQLQuery query = session.createSQLQuery(sql);

        //3 调用方法查询结果
        List<Object[]> list = query.list();
        //query.uniqueResult();

        for (Object[] objs : list) {
            System.out.println(Arrays.toString(objs));
        }

        //-------------------------------------------
        //4提交事务.关闭资源
        tx.commit();
        session.close();// 游离|托管 状态, 有id , 没有关联
    }


    //基本查询
    public void fun2() {
        //1 获得session
        Session session = HibernateUtils.openSession();
        //2 控制事务
        Transaction tx = session.beginTransaction();
        //3执行操作
        //-------------------------------------------
        //1 书写sql语句
        String sql = "SELECT * FROM cst_customer";

        //2 创建sql查询对象
        SQLQuery query = session.createSQLQuery(sql);
        //指定将结果集封装到哪个对象中
        query.addEntity(Customer.class);

        //3 调用方法查询结果
        List<Customer> list = query.list();

        System.out.println(list);
        //-------------------------------------------
        //4提交事务.关闭资源
        tx.commit();
        session.close();// 游离|托管 状态, 有id , 没有关联
    }


    //条件查询
    public void fun3() {
        //1 获得session
        Session session = HibernateUtils.openSession();
        //2 控制事务
        Transaction tx = session.beginTransaction();
        //3执行操作
        //-------------------------------------------
        //1 书写sql语句
        String sql = "SELECT * FROM cst_customer WHERE cust_id = ? ";

        //2 创建sql查询对象
        SQLQuery query = session.createSQLQuery(sql);

        query.setParameter(0, 1l);
        //指定将结果集封装到哪个对象中
        query.addEntity(Customer.class);

        //3 调用方法查询结果
        List<Customer> list = query.list();

        System.out.println(list);
        //-------------------------------------------
        //4提交事务.关闭资源
        tx.commit();
        session.close();// 游离|托管 状态, 有id , 没有关联
    }


    //分页查询
    public void fun4() {
        //1 获得session
        Session session = HibernateUtils.openSession();
        //2 控制事务
        Transaction tx = session.beginTransaction();
        //3执行操作
        //-------------------------------------------
        //1 书写sql语句
        String sql = "SELECT * FROM cst_customer  LIMIT ?,? ";

        //2 创建sql查询对象
        SQLQuery query = session.createSQLQuery(sql);

        query.setParameter(0, 0);
        query.setParameter(1, 1);
        //指定将结果集封装到哪个对象中
        query.addEntity(Customer.class);

        //3 调用方法查询结果
        List<Customer> list = query.list();

        System.out.println(list);
        //-------------------------------------------
        //4提交事务.关闭资源
        tx.commit();
        session.close();// 游离|托管 状态, 有id , 没有关联
    }

}
