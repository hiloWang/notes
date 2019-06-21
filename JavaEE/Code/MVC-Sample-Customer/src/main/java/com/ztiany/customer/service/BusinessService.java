package com.ztiany.customer.service;

import com.ztiany.customer.domain.Customer;
import com.ztiany.customer.web.bean.Page;

import java.util.List;

public interface BusinessService {

    List<Customer> queryAll();

    void addCustomer(Customer c);

    void deleteCustomer(String customerId);

    Customer findCustomer(String customerId);

    void updateCustomer(Customer c);

    /**
     * 根据用户要看的页码，返回封装了分页数据的Page对象
     *
     * @param num 页码
     */
    Page<Customer> queryPage(String num);

}
