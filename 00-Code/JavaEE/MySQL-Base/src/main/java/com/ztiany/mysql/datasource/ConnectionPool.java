package com.ztiany.mysql.datasource;

import com.ztiany.mysql.jdbc.JDBCUtils;

import java.sql.Connection;
import java.util.LinkedList;

/**
 * 简单的连接池实现
 */
public class ConnectionPool {

    private final LinkedList<Connection> mConnections = new LinkedList<>();

    public void init(int connectionSize) {
        for (int i = 0; i < connectionSize; i++) {
            mConnections.add(JDBCUtils.getConnection());
        }
    }

    public void releaseConnection(Connection connection) {
        if (connection != null) {
            synchronized (mConnections) {
                mConnections.addLast(connection);
                mConnections.notifyAll();
            }
        }
    }

    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (mConnections) {
            if (mills == 0) {
                while (mConnections.isEmpty()) {
                    mConnections.wait();
                }
                return mConnections.removeFirst();
            } else {
                long future = mills + System.currentTimeMillis();
                long remaining = mills;
                while (mConnections.isEmpty() && remaining > 0) {
                    mConnections.wait(remaining);
                    remaining = future - System.currentTimeMillis();
                }
                Connection result = null;
                if (!mConnections.isEmpty()) {
                    result = mConnections.removeFirst();
                }
                return result;
            }
        }
    }


}
