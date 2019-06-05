package me.ztiany.mybatis.test.basic;


import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.ztiany.mybatis.pojo.OrderWithUser;
import me.ztiany.mybatis.pojo.Orders;
import me.ztiany.mybatis.pojo.QueryVO;
import me.ztiany.mybatis.pojo.User;
import me.ztiany.mybatis.test.basic.mapper.OrderMapper;
import me.ztiany.mybatis.test.basic.mapper.UserMapper;

/**
 * MyBatis  + Mapper 动态代理开发(不需要开发者自己编写Mapper实现，MyBatis根据接口中定义的方法签名自动定位mapper.xml 中配置的 sql)
 */
public class MybatisMapperTest {

    private SqlSessionFactory sqlSessionFactory;

    @Before
    public void before() throws IOException {
        //加载核心配置文件
        InputStream in = Resources.getResourceAsStream("sqlMapConfig_Mapper.xml");
        //创建SqlSessionFactory
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(in);
    }

    //创建SqlSession，这里创建的Session为 org.apache.ibatis.session.defaults.DefaultSqlSession
    private SqlSession newSqlSession() {
        return sqlSessionFactory.openSession();
    }

    @Test
    public void testMapper() {
        SqlSession sqlSession = newSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        //org.apache.ibatis.binding.MapperProxy@73ad2d6
        System.out.println("UserMapper: " + userMapper);
        User user = userMapper.findUserById(10);
        System.out.println(user);
        sqlSession.close();
    }

    @Test
    public void testInsertUser() {
        //执行Sql语句
        User user = new User();
        user.setUsername("张家辉");
        user.setBirthday(new Date());
        user.setAddress("香港");
        user.setSex("男");

        SqlSession sqlSession = newSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        //返回插入后，影响的行数
        int insert = userMapper.insertUser(user);

        System.out.println("insert: " + insert);
        System.out.println("last insert user: " + user.getId());

        sqlSession.commit();
        sqlSession.close();
    }


    @Test
    public void testFindUserByQueryVO() {
        SqlSession sqlSession = newSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        QueryVO queryVO = new QueryVO();
        User user = new User();
        user.setUsername("明");
        queryVO.setUser(user);

        List<User> userList = userMapper.findUserByQueryVO(queryVO);
        System.out.println(userList);

        sqlSession.close();
    }

    @Test
    public void testCountUser() {
        SqlSession sqlSession = newSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        Integer count = userMapper.countUser();
        System.out.println("count: " + count);

        sqlSession.close();
    }

    @Test
    public void testSelectUserBySexAndUsername() {
        SqlSession sqlSession = newSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        User user = new User();
        user.setUsername(null);
        user.setSex("1");
        List<User> userList = userMapper.selectUserBySexAndUsername(user);
        System.out.println("userList: " + userList);

        sqlSession.close();
    }

    @Test
    public void testSelectUserByIDS() {
        SqlSession sqlSession = newSqlSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        List<Integer> idList = new ArrayList<>();
        idList.add(1);
        idList.add(26);
        idList.add(27);
        System.out.println("selectUserByListIds: " + userMapper.selectUserByListIds(idList));

        Integer[] idArray = new Integer[]{1, 26, 27};
        System.out.println("selectUserByArrayIds: " + userMapper.selectUserByArrayIds(idArray));

        QueryVO queryVO = new QueryVO();
        queryVO.setIdArray(idArray);
        queryVO.setIdList(idList);
        System.out.println("selectUserByQueryVOIds: " + userMapper.selectUserByQueryVOIds(queryVO));

        sqlSession.close();
    }


    @Test
    public void testSelectOrders() {
        SqlSession sqlSession = newSqlSession();
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

        List<Orders> ordersList = orderMapper.selectOrders();
        System.out.println("ordersList: " + ordersList);

        sqlSession.close();
    }

    @Test
    public void testSelectOrdersWithUser() {
        SqlSession sqlSession = newSqlSession();
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

        List<OrderWithUser> orderWithUserList = orderMapper.selectOrdersList();
        System.out.println("selectOrdersList: " + orderWithUserList);

        sqlSession.close();
    }

    @Test
    public void testSelectUserList() {
        SqlSession sqlSession = newSqlSession();
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

        List<User> userList = orderMapper.selectUserList();
        System.out.println("selectUserList: " + userList);

        sqlSession.close();
    }
}
