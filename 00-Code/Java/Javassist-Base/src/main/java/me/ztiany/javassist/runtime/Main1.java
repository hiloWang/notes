package me.ztiany.javassist.runtime;

/**
 * APP1至于当前类路径之外
 *
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class Main1 {

    public static void main(String... args) throws Exception {
        SampleLoader s = new SampleLoader();
        Class c = s.loadClass("me.ztiany.javassist.runtime.APP1");
        c.getDeclaredMethod("main", new Class[]{String[].class}).invoke(null, new Object[]{args});
    }

}
