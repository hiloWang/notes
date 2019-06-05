package me.ztiany.mvc.dao;

import org.apache.ibatis.annotations.Param;

import java.util.List;

import me.ztiany.mvc.pojo.Order;
import me.ztiany.mvc.pojo.OrderExample;

public interface OrderMapper {

    long countByExample(OrderExample example);

    int deleteByExample(OrderExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Order record);

    int insertSelective(Order record);

    List<Order> selectByExample(OrderExample example);

    Order selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Order record, @Param("example") OrderExample example);

    int updateByExample(@Param("record") Order record, @Param("example") OrderExample example);

    int updateByPrimaryKeySelective(Order record);

    int updateByPrimaryKey(Order record);
}