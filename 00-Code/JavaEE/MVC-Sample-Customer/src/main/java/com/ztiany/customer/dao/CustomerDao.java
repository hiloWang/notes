package com.ztiany.customer.dao;

import com.ztiany.customer.domain.Customer;

import java.util.List;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.26 22:45
 */
public interface CustomerDao {

    List<Customer> queryAll();

    void save(Customer c);

    void delete(String customerId);

    Customer find(String customerId);

    void update(Customer c);

    /**
     * 查询记录条数
     */
    int getTotalCount();

    /**
     * 查询分页数据
     *
     * @param startIndex 开始记录的索引。第一条记录是0
     * @param pageSize   每次取多少条
     */
    List<Customer> query(int startIndex, int pageSize);

}
