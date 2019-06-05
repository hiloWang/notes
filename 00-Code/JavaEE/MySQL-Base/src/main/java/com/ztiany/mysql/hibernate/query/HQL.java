package com.ztiany.mysql.hibernate.query;

import com.ztiany.mysql.hibernate.Customer;
import com.ztiany.mysql.hibernate.HibernateUtils;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * 测试HQL语句
 */
public class HQL {

    //基本查询
    public void fun1() {
        //1 获得session
        Session session = HibernateUtils.openSession();
        //2 控制事务
        Transaction tx = session.beginTransaction();

        //3执行操作
        //-------------------------------------------
        //1> 书写HQL语句
        //String hql = " from cn.itheima.domain.Customer ";
        String hql = " from Customer "; // 查询所有Customer对象
        //2> 根据HQL语句创建查询对象
        Query query = session.createQuery(hql);
        //3> 根据查询对象获得查询结果
        List<Customer> list = query.list();    // 返回list结果
        //query.uniqueResult();//接收唯一的查询结果

        System.out.println(list);
        //-------------------------------------------

        //4提交事务.关闭资源
        tx.commit();
        session.close();// 游离|托管 状态, 有id , 没有关联
    }

    //条件查询
    //HQL语句中,不可能出现任何数据库相关的信息的
    public void fun2() {
        //1 获得session
        Session session = HibernateUtils.openSession();
        //2 控制事务
        Transaction tx = session.beginTransaction();

        //3执行操作
        //-------------------------------------------
        //1> 书写HQL语句
        String hql = " from Customer where cust_id = 1 "; // 查询所有Customer对象
        //2> 根据HQL语句创建查询对象
        Query query = session.createQuery(hql);
        //3> 根据查询对象获得查询结果
        Customer c = (Customer) query.uniqueResult();
        System.out.println(c);
        //-------------------------------------------

        //4提交事务.关闭资源
        tx.commit();
        session.close();// 游离|托管 状态, 有id , 没有关联
    }

    //条件查询
    //问号占位符
    public void fun3() {
        //1 获得session
        Session session = HibernateUtils.openSession();
        //2 控制事务
        Transaction tx = session.beginTransaction();

        //3执行操作
        //-------------------------------------------
        //1> 书写HQL语句
        String hql = " from Customer where cust_id = ? "; // 查询所有Customer对象
        //2> 根据HQL语句创建查询对象
        Query query = session.createQuery(hql);
        //设置参数
        //query.setLong(0, 1l);
        query.setParameter(0, 1l);
        //3> 根据查询对象获得查询结果
        Customer c = (Customer) query.uniqueResult();
        System.out.println(c);
        //-------------------------------------------

        //4提交事务.关闭资源
        tx.commit();
        session.close();// 游离|托管 状态, 有id , 没有关联
    }

    //条件查询
    //命名占位符
    public void fun4() {
        //1 获得session
        Session session = HibernateUtils.openSession();
        //2 控制事务
        Transaction tx = session.beginTransaction();

        //3执行操作
        //-------------------------------------------
        //1> 书写HQL语句
        String hql = " from Customer where cust_id = :cust_id "; // 查询所有Customer对象
        //2> 根据HQL语句创建查询对象
        Query query = session.createQuery(hql);
        //设置参数
        query.setParameter("cust_id", 1l);
        //3> 根据查询对象获得查询结果
        Customer c = (Customer) query.uniqueResult();
        System.out.println(c);
        //-------------------------------------------

        //4提交事务.关闭资源
        tx.commit();
        session.close();// 游离|托管 状态, 有id , 没有关联
    }

    //分页查询
    public void fun5() {
        //1 获得session
        Session session = HibernateUtils.openSession();
        //2 控制事务
        Transaction tx = session.beginTransaction();

        //3执行操作
        //-------------------------------------------
        //1> 书写HQL语句
        String hql = " from Customer  "; // 查询所有Customer对象
        //2> 根据HQL语句创建查询对象
        Query query = session.createQuery(hql);
        //设置分页信息 limit ?,?
        query.setFirstResult(1);
        query.setMaxResults(1);
        //3> 根据查询对象获得查询结果
        List<Customer> list = query.list();
        System.out.println(list);
        //-------------------------------------------

        //4提交事务.关闭资源
        tx.commit();
        session.close();// 游离|托管 状态, 有id , 没有关联
    }

}
