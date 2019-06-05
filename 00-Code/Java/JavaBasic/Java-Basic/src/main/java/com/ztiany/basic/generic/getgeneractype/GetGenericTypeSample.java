package com.ztiany.basic.generic.getgeneractype;

/**
 * 获取泛型类型
 */
public class GetGenericTypeSample {

    public static void main(String[] args) throws Exception {
        new BeanCallBack<String>() {//这里是BeanCallBack的子类，并且指定父类BeanCallBack的类型参数为String类型
        }.parseNetworkResponse("");
    }

}
