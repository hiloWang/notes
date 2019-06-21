package me.ztiany.mybatis.test.basic.mapper;

import java.util.List;

import me.ztiany.mybatis.pojo.QueryVO;
import me.ztiany.mybatis.pojo.User;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.26 21:16
 */
public interface UserMapper {

    /*
    遵循四个原则
        1. 接口方法名  == User.xml 中 id 名
        2. 返回值类型  与  Mapper.xml文件中返回值类型要一致
        3. 方法的入参类型 与Mapper.xml中入参的类型要一致
        4. 命名空间绑定此接口
     */

    //根据id查
    User findUserById(Integer id);

    //根据用户名查找
    User findUserByUsername(String username);

    //插入User
    Integer insertUser(User user);

    //批量插入
    Integer batchInsert(List<User> userList);

    //更新User
    void updateUserById(User user);

    //删除User
    void deleteUserById(Integer userId);

    //根据VO(Value Object)查
    List<User> findUserByQueryVO(QueryVO vo);

    //查询数据条数
    Integer countUser();

    //根据性别和名字查询用户
    List<User> selectUserBySexAndUsername(User user);

    //根据id数组查询用户信息
    List<User> selectUserByArrayIds(Integer[] ids/*parameterType = array*/);

    //根据id列表查询用户信息
    List<User> selectUserByListIds(List<Integer> ids/*parameterType = list*/);

    //根据id包装类查询用户信息
    List<User> selectUserByQueryVOIds(QueryVO vo);

}
