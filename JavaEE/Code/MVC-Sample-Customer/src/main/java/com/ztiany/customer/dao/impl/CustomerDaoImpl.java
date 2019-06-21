package com.ztiany.customer.dao.impl;

import com.ztiany.customer.dao.CustomerDao;
import com.ztiany.customer.domain.Customer;
import com.ztiany.customer.utils.JDBCUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.26 22:49
 */
public class CustomerDaoImpl implements CustomerDao {

    @Override
    public List<Customer> queryAll() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM mvc_sample_customer;");
            resultSet = preparedStatement.executeQuery();
            return buildCustomers(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.release(resultSet, preparedStatement, connection);
        }
        return null;
    }

    private List<Customer> buildCustomers(ResultSet resultSet) throws SQLException {
        List<Customer> cs = new ArrayList<>();
        while (resultSet.next()) {
            Customer c = buildCustomer(resultSet);
            cs.add(c);
        }
        return cs;
    }

    private Customer buildCustomer(ResultSet resultSet) throws SQLException {
        Customer c = new Customer();
        c.setId(resultSet.getString("id"));
        c.setName(resultSet.getString("name"));
        c.setGender(resultSet.getString("gender"));
        c.setBirthday(resultSet.getDate("birthday"));
        c.setCellphone(resultSet.getString("cellphone"));
        c.setEmail(resultSet.getString("email"));
        c.setHobbies(resultSet.getString("hobbies"));
        c.setType(resultSet.getString("type"));
        c.setDescription(resultSet.getString("description"));
        return c;
    }

    @Override
    public void save(Customer c) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtils.getConnection();
            stmt = conn.prepareStatement("INSERT INTO mvc_sample_customer VALUES(?,?,?,?,?,?,?,?,?)");
            stmt.setString(1, c.getId());
            stmt.setString(2, c.getName());
            stmt.setString(3, c.getGender());
            //这里要用sql中的Date
            stmt.setDate(4, new java.sql.Date(c.getBirthday().getTime()));
            stmt.setString(5, c.getCellphone());
            stmt.setString(6, c.getEmail());
            stmt.setString(7, c.getHobbies());
            stmt.setString(8, c.getType());
            stmt.setString(9, c.getDescription());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.release(null, stmt, conn);
        }
    }

    @Override
    public void delete(String customerId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtils.getConnection();
            stmt = conn.prepareStatement("DELETE FROM mvc_sample_customer WHERE id=?");
            stmt.setString(1, customerId);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.release(null, stmt, conn);
        }
    }

    @Override
    public Customer find(String customerId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {
            conn = JDBCUtils.getConnection();
            stmt = conn.prepareStatement("SELECT * FROM mvc_sample_customer WHERE id=?");
            stmt.setString(1, customerId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                return buildCustomer(rs);
            }
            return null;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.release(rs, stmt, conn);
        }
    }

    @Override
    public void update(Customer c) {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = JDBCUtils.getConnection();
            stmt = conn.prepareStatement("UPDATE mvc_sample_customer SET name=?,gender=?,birthday=?,cellphone=?,email=?,hobbies=?,type=?,description=? WHERE id=?");
            stmt.setString(1, c.getName());
            stmt.setString(2, c.getGender());
            stmt.setDate(3, new java.sql.Date(c.getBirthday().getTime()));
            stmt.setString(4, c.getCellphone());
            stmt.setString(5, c.getEmail());
            stmt.setString(6, c.getHobbies());
            stmt.setString(7, c.getType());
            stmt.setString(8, c.getDescription());
            stmt.setString(9, c.getId());
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            JDBCUtils.release(null, stmt, conn);
        }
    }

    @Override
    public int getTotalCount() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement("SELECT COUNT(*) FROM mvc_sample_customer");
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.release(resultSet, preparedStatement, connection);
        }
        return 0;
    }

    @Override
    public List<Customer> query(int startIndex, int pageSize) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = JDBCUtils.getConnection();
            preparedStatement = connection.prepareStatement("SELECT * FROM mvc_sample_customer LIMIT ?,?;");
            preparedStatement.setInt(1, startIndex);
            preparedStatement.setInt(2, pageSize);
            resultSet = preparedStatement.executeQuery();
            return buildCustomers(resultSet);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            JDBCUtils.release(resultSet, preparedStatement, connection);
        }
        return null;
    }
}
