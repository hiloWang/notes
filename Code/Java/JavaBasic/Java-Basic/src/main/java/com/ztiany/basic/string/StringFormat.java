package com.ztiany.basic.string;

import java.util.Date;

public class StringFormat {
    public static void main(String... args) {
        testIndex();
    }

    private static void testIndex() {
        System.out.printf("%1$s %2$tB %2$tY  ", "Date", new Date());
        System.out.printf("%s %tB %<tY  ", "Date", new Date());
    }

    private static void test() {
    /*
                 %[argument_index$][flags][width][.precision]conversion
     */
        System.out.print(String.format("%-15s %5s %10s\n", "item", "Qty", "Price"));
        System.out.print(String.format("%-15s %5s %10s\n", "-----", "-----", "-----"));
        System.out.print(String.format("%-15.15s %5d %10.2f\n", "Jacks Magic Be", 4, 4.25));
        System.out.print(String.format("%-15.15s %5d %10.2f\n", "Jacks Magic Be", 5, 2.2));
    }
}
