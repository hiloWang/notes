package com.ztiany.designpattern.proxy;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.3.25 0:03
 */
public class Client {

    public static void main(String... args) {
        RealSubject realSubject = new RealSubject();
        ProxySubject proxySubject = new ProxySubject(realSubject);
        proxySubject.visit();
    }

}
