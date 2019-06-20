package com.ztiany.register.dao.impl;

import com.ztiany.register.constants.Constants;
import com.ztiany.register.dao.UserDao;
import com.ztiany.register.domain.User;
import com.ztiany.register.exception.DaoException;
import com.ztiany.register.utils.CommonUtil;
import com.ztiany.register.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.22 18:03
 */
@SuppressWarnings("all")
public class UserDaoMySQLImpl implements UserDao {

    @Override
    public void save(User user) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            stmt = conn.createStatement();
            stmt.executeUpdate("insert into " +
                    Constants.DB_NAME + " (username,password,email,birthday) values ('" + user.getUsername() + "','"
                    + user.getPassword() + "','" + user.getEmail() + "','" + CommonUtil.formatDate(user.getBirthday()) + "')");
        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            JDBCUtils.release(conn, stmt, rs);
        }
    }

    @Override
    public User findUser(String name) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {

            conn = JDBCUtils.getConnection();
            stmt = conn.createStatement();
            rs = stmt.executeQuery("SELECT * FROM " + Constants.DB_NAME + " WHERE username='" + name + "'");
            if (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setBirthday(rs.getDate("birthday"));
                return user;
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.release(conn, stmt, rs);
        }
    }

    @Override
    public User findUser(String name, String password) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            stmt = conn.createStatement();
            String sql = "select * from " + Constants.DB_NAME + " where username='" + name + "' and password='" + password + "'";
            System.out.println(sql);
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                User user = new User();
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setBirthday(rs.getDate("birthday"));
                return user;
            }
            return null;
        } catch (Exception e) {
            throw new DaoException(e);
        } finally {
            JDBCUtils.release(conn, stmt, rs);
        }
    }
}
