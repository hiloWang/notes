package com.ztiany.basic.print;

public class PrintM {

    public static void main(String[] args) {
        printM(8);
    }

    public static void printM(int n) {
        int len = n * 3 + (n - 3);
        int[][] arr = new int[n][len];
        int x = n - 1;
        int y = 0;
        boolean order = false;

        for (int a = 1; a <= len; a++) {
            arr[x][y] = a;
            if (!order) {
                x--;
            }
            if (order) {
                x++;
            }
            if (x < 0) {
                order = true;
                x = x + 2;
            }
            if (x > n - 1) {
                order = false;
                x = x - 2;
            }
            y++;

        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < len; j++) {
                if (arr[i][j] != 0)
                    System.out.print(arr[i][j]);
                else
                    System.out.print("  ");
            }
            System.out.println();
        }
    }
}
