package ztiany.chapter10;

import groovy.lang.GroovyObject;

/**
 * <br/>    功能描述：
 * <br/>    Email     : ztiany3@gmail.com
 *
 * @author Ztiany
 * @see
 * @since 1.0
 */
public class CallDynamicMethod {
    public static void main(String... args) {
        GroovyObject groovyObject = new DynamicGroovyClass();

        Object result1 = groovyObject.invokeMethod("squeak", new Object[]{});
        System.out.println(result1);

        Object result2 = groovyObject.invokeMethod("squeak", new Object[]{"like","a","duck"});
        System.out.println(result2);

        /*
        结果：
         you call squeak with []
         0
         you call squeak with [like, a, duck]
         3
         *
         */
    }
}
