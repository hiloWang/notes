package me.ztiany.compiler;

import me.ztiany.compiler.annotation.HelloTag;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class App {

    public static void main(String... args) {
        System.out.println("app is running......");

        doWork(args);

        System.out.println("app is exit......");
    }

    @HelloTag
    private static void doWork(String[] args) {
        assert args != null;
        System.out.println("App.doWork");
    }

}
