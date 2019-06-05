package test;

import org.junit.Test;

import java.io.File;

import foo.Foo;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2018/11/24 11:41
 */
public class FooTest {

    @Test
    public void testFoo() {
        File client = Foo.getCacheDir("client");
        File randomTemp = Foo.createRandomTemp(client);
        System.out.println(randomTemp.getAbsolutePath());
    }

}
