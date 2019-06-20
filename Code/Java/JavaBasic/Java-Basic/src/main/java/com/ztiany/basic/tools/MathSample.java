package com.ztiany.basic.tools;

import java.util.Random;

public class MathSample {

    private static void sop(Object obj) {
        System.out.println(obj);
    }

    public static void main(String args[]) {
        int j = Math.abs(-4);// 返回指定那个数据的绝对值
        double d = Math.ceil(12.2321);// ceil返回大于指定数据的最小整数
        sop(d);
        double d1 = Math.floor(12.32);// floor返回小于指定数据的最大整数
        sop(d1);
        long l = Math.round(12.32);// round四舍五入
        sop(l);
        double d2 = Math.pow(2, 9);// pow 幂计算
        sop(d2);

        for (int x = 0; x < 10; x++) {
            double d3 = (int) (Math.random() * 10 + 1);// 生成0包括0到1不包括1之间的伪随机数（算法可以找到规律
            // 找到规律就不随机）
            sop(d3);
        }
        sop(new Random().nextInt(10));// 一个随机的数
        Random r = new Random();// util 工具包中的Random类
        sop(r.nextInt(10));// 随机生成一个 1到10（不包括）之间的数
        sop(test(1.398232));

    }

    // 给定一个小数 保留该小数的后两位
    private static double test(double d) {
        double b1 = d * 100;//
        b1 = Math.round(b1);
        // int x=(int)b1;
        // double b2=(double)x;
        b1 = b1 / 100.0;
        return b1;
        // 1.3988 139.88 140.00
    }
}
