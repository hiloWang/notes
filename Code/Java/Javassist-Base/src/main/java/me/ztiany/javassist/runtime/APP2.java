package me.ztiany.javassist.runtime;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class APP2 {

    public static void main(String... args) {
        System.out.println(APP2.class.getClassLoader());
        System.out.println(SampleLoader.class.getClassLoader());
    }

}
