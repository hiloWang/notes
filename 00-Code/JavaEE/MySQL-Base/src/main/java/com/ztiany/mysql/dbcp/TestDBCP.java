package com.ztiany.mysql.dbcp;

import java.sql.Connection;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.29 23:57
 */
public class TestDBCP {

    public static void main(String... args){
        Connection connection = DBCPUtil.getConnection();
        System.out.println("-------------------------------------------");
        System.out.println(connection);
        System.out.println("-------------------------------------------");
        try {
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
