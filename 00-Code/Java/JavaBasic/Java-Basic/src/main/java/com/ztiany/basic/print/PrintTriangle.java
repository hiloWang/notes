package com.ztiany.basic.print;

/*
 * 打印空三角
 */
public class PrintTriangle {

    public static void main(String args[]) {
        print(9);
    }

    public static void print(int n) {
        int count = 0;
        for (int x = 0; x < n; x++) {
            ++count;
            for (int y = x; y < n; y++) {
                System.out.print(" ");
            }
            for (int z = x; z >= 0; z--) {

                if (count >= 3 && count != n) {

                    if (z == x)
                        System.out.print("* ");
                    else if (z == 0)
                        System.out.print("* ");
                    else
                        System.out.print("  ");
                } else {
                    System.out.print("* ");
                }
            }
            System.out.println();
        }


    }
}
