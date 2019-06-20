package com.ztiany.current.unsafe;

import java.lang.reflect.Field;

import sun.misc.Unsafe;

/**
 * @author Ztiany
 *         Email ztiany3@gmail.com
 *         Date 17.5.6 10:20
 */
public class UnsafeSample {

    private UnsafeSample() {
        System.out.println("UnsafeSample initialize");
    }

    public static void main(String... args) {
        try {
            Unsafe unsafe = getUnsafeInstance();
            UnsafeSample unsafeSample = (UnsafeSample) unsafe.allocateInstance(UnsafeSample.class);
            unsafeSample.printName();
        } catch (InstantiationException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    private void printName() {
        System.out.println("i am UnsafeSample");
    }


    //因为在开源版本的Unsafe.java中声明了一个实例域，所以我们可以通过反射的方式来获得这个域。
    private static Unsafe getUnsafeInstance() throws SecurityException,
            NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        Field theUnsafeInstance = Unsafe.class.getDeclaredField("theUnsafe");
        theUnsafeInstance.setAccessible(true);
        return (Unsafe) theUnsafeInstance.get(Unsafe.class);
    }

}
