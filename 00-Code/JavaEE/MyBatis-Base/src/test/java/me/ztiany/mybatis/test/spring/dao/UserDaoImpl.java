package me.ztiany.mybatis.test.spring.dao;

import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.support.SqlSessionDaoSupport;

import me.ztiany.mybatis.pojo.User;
import me.ztiany.mybatis.test.basic.mapper.UserMapper;

/**
 * 原始Dao开发
 */
public class UserDaoImpl extends SqlSessionDaoSupport implements UserDao {

    @Override
    public int countUser() {
        SqlSession sqlSession = getSqlSession();
        //org.mybatis.spring.SqlSessionTemplate
        System.out.println(sqlSession);
        return sqlSession.getMapper(UserMapper.class).countUser();
    }

    @Override
    public int insertUser(User user) {
        SqlSession sqlSession = getSqlSession();
        //org.mybatis.spring.SqlSessionTemplate
        System.out.println(sqlSession);
        return sqlSession.getMapper(UserMapper.class).insertUser(user);
    }

}
