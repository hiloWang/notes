package com.ztiany.mysql.assist;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

/**
 * 使用前提：JavaBean的字段名和表格的字段名保持一致，封装一个对象，适合结果集只有一条记录
 */
public class BeanHandler implements ResultSetHandler {

    private Class clazz;

    @SuppressWarnings("all")
    public BeanHandler(Class clazz) {
        this.clazz = clazz;
    }

    public Object handle(ResultSet rs) {
        try {
            Object bean = null;
            //把结果集中的数据封装到JavaBean中
            if (rs.next()) {
                bean = clazz.newInstance();
                ResultSetMetaData metaData = rs.getMetaData();
                int columnCount = metaData.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    String fieldName = metaData.getColumnName(i + 1);//列名
                    Object fieldValue = rs.getObject(i + 1);//列值
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);//强暴
                    field.set(bean, fieldValue);
                }
            }
            return bean;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
