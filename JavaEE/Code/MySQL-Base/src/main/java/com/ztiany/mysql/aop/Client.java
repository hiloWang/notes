package com.ztiany.mysql.aop;


import com.ztiany.mysql.aop.service.BusinessService;
import com.ztiany.mysql.aop.tools.BeanFactory;

public class Client {

    public static void main(String[] args) {
        BusinessService service = BeanFactory.getBusinessService();
        service.transfer("aaa", "bbb", 100);
    }

}
