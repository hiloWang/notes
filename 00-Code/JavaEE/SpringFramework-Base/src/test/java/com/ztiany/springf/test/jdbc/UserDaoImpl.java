package com.ztiany.springf.test.jdbc;


import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;


//使用JDBC模板实现增删改查
public class UserDaoImpl extends JdbcDaoSupport implements UserDao {

    @Override
    public void save(User u) {
        String sql = "INSERT INTO springf_test_user VALUES(NULL,?) ";
        getJdbcTemplate().update(sql, u.getName());
    }

    @Override
    public void delete(Integer id) {
        String sql = "DELETE FROM springf_test_user WHERE id = ? ";
        getJdbcTemplate().update(sql, id);
    }

    @Override
    public void update(User u) {
        String sql = "UPDATE  springf_test_user SET name = ? WHERE id=? ";
        getJdbcTemplate().update(sql, u.getName(), u.getId());
    }

    @Override
    @SuppressWarnings("all")
    public User getById(Integer id) {
        String sql = "SELECT * FROM springf_test_user WHERE id = ? ";
        return getJdbcTemplate().queryForObject(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                return u;
            }
        }, id);

    }

    @Override
    public int getTotalCount() {
        String sql = "SELECT count(*) FROM springf_test_user  ";
        return getJdbcTemplate().queryForObject(sql, Integer.class);
    }

    @Override
    @SuppressWarnings("all")
    public List<User> getAll() {
        String sql = "SELECT * FROM springf_test_user  ";
        return getJdbcTemplate().query(sql, new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User u = new User();
                u.setId(rs.getInt("id"));
                u.setName(rs.getString("name"));
                return u;
            }
        });
    }

}
