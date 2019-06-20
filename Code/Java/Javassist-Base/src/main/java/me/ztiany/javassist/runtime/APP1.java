package me.ztiany.javassist.runtime;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class APP1 {

    public static void main(String... args) {
        System.out.println(APP1.class.getClassLoader());
        System.out.println(SampleLoader.class.getClassLoader());
    }

}
