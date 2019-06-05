package com.ztiany.mysql.hibernate.query;

import com.ztiany.mysql.hibernate.Customer;
import com.ztiany.mysql.hibernate.HibernateUtils;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * 测试Criteria查询
 */
public class CriteriaQL {
    
    //基本查询
    public void fun1() {
        //1 获得session
        Session session = HibernateUtils.openSession();
        //2 控制事务
        Transaction tx = session.beginTransaction();
        //3执行操作
        //-------------------------------------------
        //查询所有的Customer对象
        Criteria criteria = session.createCriteria(Customer.class);
        List<Customer> list = criteria.list();
        System.out.println(list);


        //-------------------------------------------
        //4提交事务.关闭资源
        tx.commit();
        session.close();// 游离|托管 状态, 有id , 没有关联
    }


    //条件查询
    //HQL语句中,不可能出现任何数据库相关的信息的
    // >       gt
    // >=      ge
    // <      lt
    // <=      le
    // ==      eq
    // !=      ne
    // in      in
    // between and   between
    // like     like
    // is not null    isNotNull
    // is null     isNull
    // or      or
    // and      and
    public void fun2() {
        //1 获得session
        Session session = HibernateUtils.openSession();
        //2 控制事务
        Transaction tx = session.beginTransaction();
        //3执行操作
        //-------------------------------------------
        //创建criteria查询对象
        Criteria criteria = session.createCriteria(Customer.class);
        //添加查询参数 => 查询cust_id为1的Customer对象
        criteria.add(Restrictions.eq("cust_id", 1l));
        //执行查询获得结果
        Customer c = (Customer) criteria.uniqueResult();
        System.out.println(c);
        //-------------------------------------------
        //4提交事务.关闭资源
        tx.commit();
        session.close();// 游离|托管 状态, 有id , 没有关联
    }


    //分页查询
    public void fun3() {
        //1 获得session
        Session session = HibernateUtils.openSession();
        //2 控制事务
        Transaction tx = session.beginTransaction();
        //3执行操作
        //-------------------------------------------
        //创建criteria查询对象
        Criteria criteria = session.createCriteria(Customer.class);
        //设置分页信息 limit ?,?
        criteria.setFirstResult(1);
        criteria.setMaxResults(2);
        //执行查询
        List<Customer> list = criteria.list();

        System.out.println(list);
        //-------------------------------------------
        //4提交事务.关闭资源
        tx.commit();
        session.close();// 游离|托管 状态, 有id , 没有关联
    }


    //查询总记录数
    public void fun4() {
        //1 获得session
        Session session = HibernateUtils.openSession();
        //2 控制事务
        Transaction tx = session.beginTransaction();
        //3执行操作
        //-------------------------------------------
        //创建criteria查询对象
        Criteria criteria = session.createCriteria(Customer.class);
        //设置查询的聚合函数 => 总行数
        criteria.setProjection(Projections.rowCount());
        //执行查询
        Long count = (Long) criteria.uniqueResult();

        System.out.println(count);
        //-------------------------------------------
        //4提交事务.关闭资源
        tx.commit();
        session.close();// 游离|托管 状态, 有id , 没有关联
    }

}
