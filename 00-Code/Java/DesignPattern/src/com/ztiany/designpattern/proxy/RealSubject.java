package com.ztiany.designpattern.proxy;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.3.25 0:02
 */
public class RealSubject implements Subject {

    @Override
    public void visit() {
        System.out.println("real subject ");
    }
}
