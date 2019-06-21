package com.ztiany.mysql.aop.tools;

import com.ztiany.mysql.aop.service.BusinessService;
import com.ztiany.mysql.aop.service.BusinessServiceImpl;

import java.lang.reflect.Proxy;

/**
 * AOP核心思想，返回代理
 */
public class BeanFactory {

    public static BusinessService getBusinessService() {

        final BusinessService s = new BusinessServiceImpl();//原有对象

        return (BusinessService) Proxy.newProxyInstance(s.getClass().getClassLoader(),
                s.getClass().getInterfaces(), (proxy, method, args) -> {
                    try {
                        TransactionManager.startTransaction();
                        Object rtValue = method.invoke(s, args);
                        TransactionManager.commit();
                        return rtValue;
                    } catch (Exception e) {
                        TransactionManager.rollback();
                        throw new RuntimeException(e);
                    } finally {
                        TransactionManager.release();
                    }
                });
    }

}
