package com.ztiany.mall.utils;


import com.ztiany.mall.config.AppConfig;
import com.ztiany.mall.service.ProductService;
import com.ztiany.mall.service.UserService;
import com.ztiany.mall.service.impl.UserServiceImpl;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.lang.reflect.Proxy;

/**
 * AOP核心思想，返回代理
 */
public class BeanFactory {

    public static UserService getUserService() {
        final UserService s = new UserServiceImpl();//原有对象
        return proxyIt(s);
    }

    public static ProductService getProductService() {
        final ProductService productService = getBean(AppConfig.PRODUCT_SERVICE);
        return proxyIt(productService);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getBean(String beanName) {
        //生产对象---根据清单生产----配置文件----将每一个bean对象的生产的细节配到配置文件中
        //使用dom4j的xml解析
        try {
            //1、创建解析器
            SAXReader reader = new SAXReader();
            //2、解析文档---bean.xml在src下
            String path = BeanFactory.class.getClassLoader().getResource("bean.xml").getPath();
            Document doc = reader.read(path);
            //3、获得元素---参数是xpath规则
            Element element = (Element) doc.selectSingleNode("//bean[@id='" + beanName + "']");
            String className = element.attributeValue("class");
            //使用反射创建对象
            Class clazz = Class.forName(className);
            return (T) clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalStateException("service not found: " + beanName);
        }
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxyIt(T s) {
        return (T) Proxy.newProxyInstance(s.getClass().getClassLoader(), s.getClass().getInterfaces(),
                (proxy, method, args) -> {
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
