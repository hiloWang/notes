package com.ztiany.mysql.assist;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

/**
 * 简单的数据库操作封装
 */
public class DBAssist {

    private DataSource dataSource;

    public DBAssist(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void update(String sql, Object... params) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            setStatementArgsChecked(stmt, params);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            release(null, stmt, conn);
        }
    }

    //执行查询语句：封装到JavaBean中
    public Object query(String sql, ResultSetHandler handler, Object... params) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.prepareStatement(sql);
            //设置参数
            setStatementArgsChecked(stmt, params);
            rs = stmt.executeQuery();
            //策略模式
            return handler.handle(rs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            release(rs, stmt, conn);
        }
    }


    private void setStatementArgsChecked(PreparedStatement stmt, Object[] params) throws SQLException {
        //设置参数
        ParameterMetaData pmd = stmt.getParameterMetaData();
        int paramCount = pmd.getParameterCount();
        if (paramCount > 0) {
            if (params == null) {
                throw new RuntimeException("参数不对");
            }
            if (paramCount != params.length) {
                throw new RuntimeException("参数数量不对");
            }
            for (int i = 0; i < paramCount; i++) {
                stmt.setObject(i + 1, params[i]);
            }
        }
    }

    @SuppressWarnings("all")
    private void release(ResultSet rs, Statement stmt, Connection conn) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs = null;
        }

        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            stmt = null;
        }

        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn = null;
        }
    }
}
