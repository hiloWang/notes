package com.ztiany.mysql.datasource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * JDBC规范：自定义数据源，实现SQL连接池
 * <pre>
 *     这里利用的包装模式，使用动态代理也是可以实现的。
 * </pre>
 */
public class CustomConnection extends ConnectionAdapter {

    private Connection conn;
    private List<Connection> pool;

    public CustomConnection(Connection conn, List<Connection> pool) {
        super(conn);
        this.conn = conn;
        this.pool = pool;
    }

    /**
     * 外界调用的关闭操作，实际上为规范连接到连接池中
     */
    @Override
    public void close() throws SQLException {
        pool.add(conn);
    }

}
