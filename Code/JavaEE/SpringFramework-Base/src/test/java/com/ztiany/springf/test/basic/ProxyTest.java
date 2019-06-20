package com.ztiany.springf.test.basic;

import org.junit.Test;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;

import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * 演示 Proxy 和 CGLib 代理
 */
public class ProxyTest {

    public interface FakeService {
        void save(String name);
    }

    public static class BaseFakeService implements FakeService {

        public BaseFakeService() {

        }

        @Override
        public void save(String name) {
            System.out.println("save " + name);
        }
    }

    @Test
    public void testProxy() {
        FakeService fakeService = name -> System.out.println("testProxy realService save " + name);

        FakeService proxyFakeService = (FakeService) Proxy.newProxyInstance(
                FakeService.class.getClassLoader(),
                fakeService.getClass().getInterfaces(),
                (proxy, method, args) -> {
                    System.out.println("testProxy proxy save " + Arrays.toString(args));
                    return null;
                });

        proxyFakeService.save("abc");
        System.out.println(proxyFakeService instanceof FakeService);
    }

    @Test
    public void testCglib() {

        Enhancer en = new Enhancer();//帮我们生成代理对象

        en.setSuperclass(BaseFakeService.class);//设置对谁进行代理

        en.setCallback((MethodInterceptor) (o, method, objects, methodProxy) -> {
            System.out.println("testCglib proxy save " + Arrays.toString(objects));
            return null;
        });//代理要做什么

        FakeService baseFakeService = (FakeService) en.create();//创建代理对象

        baseFakeService.save("abc");
        System.out.println(baseFakeService instanceof FakeService);

    }
}
