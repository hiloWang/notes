package com.ztiany.mysql.dbcp;

import org.apache.commons.dbcp2.BasicDataSourceFactory;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import javax.sql.DataSource;

/**
 * DBCP连接池管理，从类路径中读取配置文件
 */
public class DBCPUtil {

    private static DataSource dataSource;

    static {
        try {
            Properties props = new Properties();
            props.load(DBCPUtil.class.getClassLoader().getResourceAsStream("dbcpconfig.properties"));
            //根据配置文件创建数据源
            dataSource = BasicDataSourceFactory.createDataSource(props);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ExceptionInInitializerError("创建数据源失败");
        }
    }

    public static DataSource getDataSource() {
        return dataSource;
    }

    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
