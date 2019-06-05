package com.ztiany.designpattern.proxy;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.3.25 0:02
 */
public class ProxySubject implements Subject {

    private final Subject mProxy;

    public ProxySubject(Subject proxy) {
        mProxy = proxy;
    }

    @Override
    public void visit() {
        //通过真实主题引用的对象调用真实主题中的逻辑方法
        mProxy.visit();
    }
}
