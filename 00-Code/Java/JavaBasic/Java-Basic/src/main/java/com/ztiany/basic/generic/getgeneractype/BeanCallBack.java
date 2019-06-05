package com.ztiany.basic.generic.getgeneractype;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@SuppressWarnings("unchecked")
abstract class BeanCallBack<T> extends AbsCallback<T> {

    @Override
    public T parseNetworkResponse(Object response) throws Exception {
        Type type = this.getClass().getGenericSuperclass();//返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        System.out.println(type);
        if (type instanceof ParameterizedType) {//如果type是参数化类型

            ParameterizedType parameterizedType = (ParameterizedType) type;
            Type beanType = parameterizedType.getActualTypeArguments()[0];
            System.out.println(beanType);

            if (beanType == String.class) {
                //如果是String类型，直接返回字符串
                return (T) response;
            } else {
                //如果是 Bean List Map ，则解析完后返回
                return null;
            }
        } else {
            //如果没有写泛型，直接返回Response对象
            return (T) response;
        }
    }
}