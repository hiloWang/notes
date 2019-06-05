package com.ztiany.customer.service.impl;

import com.ztiany.customer.dao.CustomerDao;
import com.ztiany.customer.domain.Customer;
import com.ztiany.customer.service.BusinessService;
import com.ztiany.customer.utils.DaoFactory;
import com.ztiany.customer.web.bean.Page;

import java.util.List;
import java.util.UUID;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.26 23:39
 */
public class BusinessServiceImpl implements BusinessService {

    private CustomerDao dao = DaoFactory.newUserDao();

    @Override
    public List<Customer> queryAll() {
        return dao.queryAll();
    }

    @Override
    public void addCustomer(Customer c) {
        if (c == null) {
            throw new IllegalArgumentException("参数不全");
        }
        c.setId(UUID.randomUUID().toString());
        dao.save(c);
    }

    @Override
    public void deleteCustomer(String customerId) {
        dao.delete(customerId);
    }

    @Override
    public Customer findCustomer(String customerId) {
        return dao.find(customerId);
    }

    @Override
    public void updateCustomer(Customer c) {
        if (c == null || c.getId() == null) {
            throw new IllegalArgumentException("参数不全");
        }
        dao.update(c);
    }

    @Override
    public Page<Customer> queryPage(String num) {
        int currentPage = 1;
        if (num != null && !num.equals("")) {
            try {
                currentPage = Integer.parseInt(num);
            } catch (Exception e) {
                throw new RuntimeException("页码必须是数字");
            }
        }
        int totalRecords = dao.getTotalCount();
        Page<Customer> page = new Page<>(currentPage, totalRecords);
        List<Customer> record = dao.query(page.getStartIndex(), page.getPageSize());
        page.setRecord(record);
        return page;

    }
}
