package me.ztiany.bean.xml;

import java.util.List;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.12 23:57
 */
public class BeanB {

    private List<String> mList;
    private BeanA mBeanA;
    private int mAge;
    private String mName;

    public void setList(List<String> list) {
        mList = list;
    }

    public void setBeanA(BeanA beanA) {
        mBeanA = beanA;
    }

    public void setAge(int age) {
        mAge = age;
    }

    public void setName(String name) {
        mName = name;
    }
}
