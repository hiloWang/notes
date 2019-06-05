package com.ztiany.mysql.c3p0;

import java.sql.Connection;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.29 23:48
 */
public class TestC3P0 {

    public static void main(String... args){
        Connection connection = C3P0Util.getConnection();
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
