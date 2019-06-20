package ztiany.chapter3;

import java.util.ArrayList;
import java.util.Collection;

public class UsingCollectionJava {

    public static void main(String... args) {
        ArrayList<String> list = new ArrayList<>();
        Collection<String> col = list;
        list.add("one");
        list.add("two");
        list.add("three");

        list.remove(0);
        col.remove(0);//因为这里的引用是Collection，没有remove(index)的方法，所以这里调用的是remove(Object)方法
        System.out.println(list.size());
        System.out.println(col.size());

    }
}