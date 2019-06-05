package com.ztiany.xml.dom4j.sample.util;

import com.ztiany.xml.dom4j.sample.dao.StudentDao;

import java.io.InputStream;
import java.util.Properties;


//负责产生DAO实例的工厂:层间解耦（必须面向接口编程）
public class DaoObjectFactory {
    //静态工厂
    public static StudentDao getStudentDao() {
        try {
            InputStream inStream = DaoObjectFactory.class.getClassLoader().getResourceAsStream("daoObj.properties");
            Properties props = new Properties();
            props.load(inStream);
            String implDaoFullClassName = props.getProperty("studentDao");
            return (StudentDao) Class.forName(implDaoFullClassName).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
