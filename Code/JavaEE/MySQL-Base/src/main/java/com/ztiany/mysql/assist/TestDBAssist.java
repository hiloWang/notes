package com.ztiany.mysql.assist;

import com.ztiany.mysql.dbcp.DBCPUtil;

/**
 * 测试自己编写的 DB工具类
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.30 0:10
 */
public class TestDBAssist {

    public static void main(String... args) {

        DBAssist dbAssist = new DBAssist(DBCPUtil.getDataSource());
        Object query = dbAssist.query("select * from account;", new BeanListHandler(Account.class));
        System.out.println(query);

    }

    public static class Account {
        private int id;
        private String name;
        private String money;

        @Override
        public String toString() {
            return "Account{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", money='" + money + '\'' +
                    '}';
        }
    }

}
