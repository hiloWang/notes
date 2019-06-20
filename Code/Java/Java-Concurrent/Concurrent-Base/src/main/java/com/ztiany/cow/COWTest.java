package com.ztiany.cow;

import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class COWTest {

    public static void main(String... args) {
        CopyOnWriteArrayList<String> cow = new CopyOnWriteArrayList<>();
        for (int i = 0; i < 100; i++) {
            cow.add(String.valueOf(i));
        }


       int size = cow.size();
        for (int i = 0; i < size; i++) {
            System.out.println(cow.get(i));
            cow.remove(i);
        }

        System.out.println("---------------1");
        for (String s : cow) {
            cow.remove(s);
            System.out.println(s);
        }
        System.out.println("---------------2");
        for (String s : cow) {
            cow.remove(s);
            System.out.println(s);
        }
    }


}
