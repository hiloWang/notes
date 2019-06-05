package com.ztiany.basic.print;

public class PrintMatrix {

    private static int l, r, u, d;// 定义上下左右边界
    private static int[][] a = new int[0][0];// 数组
    private static int y = 0, x = 0;// 初始化坐标
    private static int step, temp;// 步长,上一个数组的值

    private static void add(int y, int x) {
        temp += step;
        a[y][x] = temp;
    }

    private static void left() {
        while (x > l)
            add(y, --x);// 未遇到左边界
        if (y > u) {// 遇到左边界且未遇到上边界
            l++;
            up();
        }
    }

    private static void right() {
        while (x < r)
            add(y, ++x);// 未遇到右边界
        if (y < d) {// 遇到右边界且未遇到下边界
            r--;
            down();
        }
    }

    private static void up() {
        while (y > u)
            add(--y, x);// 未遇到上边界
        if (x < r) {// 遇到上边界且未遇到右边界
            u++;
            right();
        }
    }

    private static void down() {
        while (y < d)
            add(++y, x);// 未遇到下边界
        if (x > l) {// 遇到下边界且未遇到左边界
            d--;
            left();
        }
    }

    public static void spiralArray(int start, int step, int length) {// 产生数组（起始值，步长，矩阵边长）
        PrintMatrix.step = step;
        l = u = 0;
        r = d = length - 1;
        a = new int[length][length];
        a[0][0] = temp = start;
        up();
    }

    public static void spiralArray(int i) {
        spiralArray(0, 1, i);
    }

    public static void print() {// 输出矩阵
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a.length; j++) {
                System.out.print(a[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        spiralArray(12);
        print();
    }
}
