package com.ztiany.basic.proxy;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * <pre>
 *          代理
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-02-18 21:49
 *         Email: ztiany3@gmail.com
 */
public class ProxySample {


    public static void main(String... args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {

        Class clazzProxy = Proxy.getProxyClass(Collection.class.getClassLoader(), Collection.class);
        System.out.println(clazzProxy.getName());

        for (Constructor constructor : clazzProxy.getConstructors()) {
            System.out.println(constructor.getName());
        }
        for (Method method : clazzProxy.getMethods()) {
            System.out.println(method.getName());
        }

        System.out.println("================================");

        List<String> list = new ArrayList<>();
        List proxy = (List) getProxy(list, new Advice() {
            @Override
            public void beforeMethod(Method method) {
                System.out.println("call before "+method.getName());
            }

            @Override
            public void afterMethod(Method method) {
                System.out.println("call after  " + method.getName());
            }
        });
        proxy.add(1);
        System.out.println(proxy.hashCode());
        System.out.println(list.size());
        System.out.println(proxy.getClass());

    }

    private static Object getProxy(final Object target, final Advice advice) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                (arg, arg1, arg2) -> {
                    advice.beforeMethod(arg1);
                    Object retVal = arg1.invoke(target, arg2);
                    advice.afterMethod(arg1);
                    return retVal;
                });
    }

    interface Advice {

        void beforeMethod(Method method);//方法一般接收三个参数  target method args

        void afterMethod(Method method);

    }

}
