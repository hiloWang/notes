package com.ztiany.mysql.aop.tools;

import com.ztiany.mysql.dbcp.DBCPUtil;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * 事务管理器：管理链接和所有事务
 */
public class TransactionManager {

    private static ThreadLocal<Connection> tl = new ThreadLocal<>();

    public static Connection getConnection() {
        Connection conn = tl.get();//从当前线程上获取链接
        if (conn == null) {
            conn = DBCPUtil.getConnection();
            tl.set(conn);//绑定到当前线程上
        }
        return conn;
    }

    public static void startTransaction() {
        try {
            Connection conn = getConnection();
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void commit() {
        try {
            Connection conn = getConnection();
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void rollback() {
        try {
            Connection conn = getConnection();
            conn.rollback();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void release() {
        try {
            Connection conn = getConnection();
            conn.close();
            tl.remove();//从当前线程上删除链接。服务器用了线程池
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
