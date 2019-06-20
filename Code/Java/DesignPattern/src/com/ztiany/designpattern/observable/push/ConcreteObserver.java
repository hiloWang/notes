package com.ztiany.designpattern.observable.push;

/**
 * <br/>    功能描述：
 * <br/>    Email     : ztiany3@gmail.com
 *
 * @author Ztiany
 * @see
 * @since 1.0
 */
public class ConcreteObserver implements Observer {
    @Override
    public void update(String content) {
        System.out.println("收到了更新的通知：" + content);
    }
}
