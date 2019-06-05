package com.ztiany.mysql.assist;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用前提：JavaBean的字段名和表格的字段名保持一致，封装多条查询记录
 */
public class BeanListHandler implements ResultSetHandler {

    private BeanHandler mBeanHandler;

    public BeanListHandler(Class clazz) {
        mBeanHandler = new BeanHandler(clazz);
    }

    @SuppressWarnings("unchecked")
    public Object handle(ResultSet rs) {
        try {
            List list = new ArrayList();
            //把结果集中的数据封装到JavaBean中
            Object bean;
            while ((bean = mBeanHandler.handle(rs)) != null) {
                list.add(bean);
            }
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
