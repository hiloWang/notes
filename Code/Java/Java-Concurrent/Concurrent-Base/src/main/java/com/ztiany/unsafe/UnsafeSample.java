package com.ztiany.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * 具体用法可以参考：https://blog.csdn.net/javazejian/article/details/72772470
 *
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class UnsafeSample {

    /*
    Unsafe类存在于sun.misc包中，其内部方法操作可以像C的指针一样直接操作内存
        该类是非安全的，Unsafe拥有着类似于C的指针操作
        Java官方不建议直接使用的Unsafe类，Oracle正在计划从Java 9中去掉Unsafe类
        Java中CAS操作的执行依赖于Unsafe类的方法
        Unsafe类中的所有方法都是native修饰的，直接调用操作系统底层资源执行相应任务
     */

    private Unsafe unsafe;

    UnsafeSample() {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            unsafe = (Unsafe) theUnsafe.get(null);
            System.out.println(unsafe);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void run() throws InstantiationException {
        Person person = (Person) unsafe.allocateInstance(Person.class);
        System.out.println(person);
    }

    public static void main(String... args) throws InstantiationException {
        UnsafeSample unsafeSample = new UnsafeSample();
        unsafeSample.run();
    }

}
