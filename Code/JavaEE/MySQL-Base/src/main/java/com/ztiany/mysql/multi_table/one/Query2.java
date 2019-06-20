package com.ztiany.mysql.multi_table.one;

import com.ztiany.mysql.jdbc.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.12 23:54
 */
public class Query2 {

    public static void main(String... args) {
        Connection conn = null;
        PreparedStatement preparedStatement = null;
        ResultSet rs = null;
        try {
            // 1.获取连接
            conn = JDBCUtils.getConnection();
            // 2.编写sql语句
            String sql = "SELECT * FROM weather WHERE temperature > ?";
            // 3.获取执行sql语句对象
            preparedStatement = conn.prepareStatement(sql);
            // 4.设置参数
            preparedStatement.setString(1, "30");
            // 5.执行更新操作
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                System.out.println("城市：" + rs.getString("city") + " 温度：" + rs.getString("temperature"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            // 6.释放资源
            JDBCUtils.release(conn, preparedStatement, rs);
        }
    }
}
