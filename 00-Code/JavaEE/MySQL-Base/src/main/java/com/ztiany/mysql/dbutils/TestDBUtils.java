package com.ztiany.mysql.dbutils;

import com.ztiany.mysql.dbcp.DBCPUtil;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.KeyedHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@SuppressWarnings("all")
public class TestDBUtils {

    public static void main(String... args) throws Exception {
        TestDBUtils resultSetHandlerTest = new TestDBUtils();

        //resultSetHandlerTest.testAdd();
        //resultSetHandlerTest.testUpdate();
        resultSetHandlerTest.test1();
        resultSetHandlerTest.test2();
        resultSetHandlerTest.test3();
        resultSetHandlerTest.test4();
        resultSetHandlerTest.test5();
        resultSetHandlerTest.test6();
        resultSetHandlerTest.test7();

    }

    private QueryRunner qr = new QueryRunner(DBCPUtil.getDataSource());

    public void testAdd() throws SQLException {
        qr.update("insert into dbu_user values(?,?,?)", 1, "aa", "2002-01-05");
        qr.update("insert into dbu_user values(?,?,?)", 2, "bb", "2001-09-08");
    }

    public void testUpdate() throws SQLException {
        qr.update("update dbu_user set name=? where id=?", "aaa", 1);
    }

    //ArrayHandler：把结果集中的第一行数据转成对象数组.元素就是每列的值
    public void test1() throws Exception {
        System.out.println("TestDBUtils.test1---------------------------------------------");

        Object objArr[] = qr.query("select * from dbu_user", new ArrayHandler());
        for (Object obj : objArr)
            System.out.println(obj);
    }

    //ArrayListHandler：适合结果有多条。List<Object[]>
    public void test2() throws Exception {
        System.out.println("TestDBUtils.test2---------------------------------------------");

        List<Object[]> list = qr.query("select * from dbu_user", new ArrayListHandler());
        for (Object[] objs : list)
            for (Object obj : objs)
                System.out.println(obj);
    }

    //ColumnListHandler：将结果集中某一列的数据存放到List中。List<Object>
    public void test3() throws Exception {
        System.out.println("TestDBUtils.test3---------------------------------------------");

        ArrayList<Object> name = (ArrayList<Object>) qr.query("select * from dbu_user", new ColumnListHandler("name"));
        System.out.println(name);
    }

    //KeyedHandler:将结果集中的每一行数据都封装到一个Map<列名,列值>里，再把这些map再存到一个map里，其key为指定的key
    public void test4() throws Exception {
        System.out.println("TestDBUtils.test4---------------------------------------------");

        Object id = qr.query("select * from dbu_user", new KeyedHandler("id"));
        Map<Object, Map<String, Object>> bmap = (Map<Object, Map<String, Object>>) id;
        for (Map.Entry<Object, Map<String, Object>> bme : bmap.entrySet()) {
            for (Map.Entry<String, Object> lme : bme.getValue().entrySet()) {
                System.out.println(lme.getKey() + ":" + lme.getValue());
            }
        }
    }

    //MapHandler：将结果集中的第一行数据封装到一个Map里，key是列名，value就是对应的值
    public void test5() throws Exception {
        System.out.println("TestDBUtils.test5---------------------------------------------");
        Map<String, Object> map = qr.query("select * from dbu_user", new MapHandler());
        for (Map.Entry<String, Object> lme : map.entrySet()) {
            System.out.println(lme.getKey() + ":" + lme.getValue());
        }
    }

    //MapListHandler：将结果集中的每一行数据都封装到一个Map里，然后再存放到List
    public void test6() throws Exception {
        System.out.println("TestDBUtils.test6---------------------------------------------");
        List<Map<String, Object>> list = qr.query("select * from dbu_user", new MapListHandler());
        for (Map<String, Object> map : list)
            for (Map.Entry<String, Object> lme : map.entrySet()) {
                System.out.println(lme.getKey() + ":" + lme.getValue());
            }
    }

    //ScalarHandler:适合结果有一行一列
    public void test7() throws Exception {
        System.out.println("TestDBUtils.test7---------------------------------------------");
        Object obj = qr.query("select count(*) from dbu_user", new ScalarHandler(1));
        //System.out.println(obj.getClass().getName());
        System.out.println(obj);
        if (obj instanceof Long) {
            Long l = (Long) obj;
            System.out.println(l.intValue());
        }
    }
}
