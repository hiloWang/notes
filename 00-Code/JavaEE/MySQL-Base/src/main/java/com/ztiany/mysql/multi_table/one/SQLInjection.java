package com.ztiany.mysql.multi_table.one;

import com.ztiany.mysql.jdbc.JdbcUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 测试sql注入问题
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.11 0:32
 */
public class SQLInjection {

    public void testLogin() {
        try {
            login1("zs' or 'zs", "zs");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void login(String username, String password) throws SQLException {
        Connection connection = JdbcUtil.getConnection();
        Statement stmt = connection.createStatement();
        String sql = "select * from tbl_user where " + "uname='" + username + "' and upassword='" + password + "'";
        ResultSet rs = stmt.executeQuery(sql);
        if (rs.next()) {
            System.out.println("恭喜您，" + username + ",登录成功!");
            System.out.println(sql);
        } else {
            System.out.println("账号或密码错误!");
        }
        JdbcUtil.release(rs, stmt, connection);
    }

    private void login1(String username, String password) throws SQLException {
        String sql = "select * from tbl_user where uname=? and upassword=?";
        Connection connection = JdbcUtil.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            System.out.println("恭喜您，" + username + ",登录成功!");
            System.out.println(sql);
        } else {
            System.out.println("账号或密码错误!");
        }
        JdbcUtil.release(rs, preparedStatement, connection);
    }
}


