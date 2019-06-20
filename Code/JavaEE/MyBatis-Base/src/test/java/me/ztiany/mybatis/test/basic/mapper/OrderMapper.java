package me.ztiany.mybatis.test.basic.mapper;

import java.util.List;

import me.ztiany.mybatis.pojo.OrderWithUser;
import me.ztiany.mybatis.pojo.Orders;
import me.ztiany.mybatis.pojo.User;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.5.27 16:23
 */
public interface OrderMapper {

    //查询订单表order的所有数据
    Integer insertOrder(Orders orders);

    //查询订单表order的所有数据
    List<Orders> selectOrders();

    //一对一关联 查询，以订单为中心关联用户
    List<OrderWithUser> selectOrdersList();

    //一对多关联
    List<User> selectUserList();


}
