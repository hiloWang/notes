package me.ztiany.mybatis.test.basic.dao;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;

import me.ztiany.mybatis.pojo.User;

public class UserDaoImpl implements UserDao {

    //注入
    private SqlSessionFactory sqlSessionFactory;

    public UserDaoImpl(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    //通过用户ID查询一个用户
    @Override
    public User selectUserById(Integer id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        return sqlSession.selectOne("test.findUserById", id);
    }

    //通过用户名称模糊查询
    @Override
    public List<User> selectUserByUsername(Integer id) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        List<User> userList = sqlSession.selectList("test.findUserById", id);
        sqlSession.close();
        return userList;
    }

    //插入用户
    @Override
    public Integer insertUser(User user) {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        int lastInsertId = sqlSession.insert("test.insertUser", user);
        sqlSession.commit();
        sqlSession.close();
        return lastInsertId;
    }
}
