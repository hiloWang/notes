package com.ztiany.basic.print;

import java.util.ArrayList;
import java.util.List;


public class PrintGroup {

    //给出一个字符串 没有重复的字符  打印出所有的组合 如： abc ： a b c ab ac ba bc ca cb abc acb bac bca cab cba
    public static void main(String[] args) {
        showPermString("abcuhdfs");
    }

    public static void showPermString(String str) {
        for (int i = 1; i <= str.length(); i++) {
            List<String> list = new ArrayList<String>();
            perm(list, str.toCharArray(), 0, i);
            System.out.println(list);
        }
    }

    public static void perm(List<String> list, char[] chs, int k, int len) {
        if (k == chs.length) {
            String string = String.valueOf(chs, 0, len);
            if (!list.contains(string))
                list.add(string);
        } else {
            for (int i = k; i < chs.length; i++) {
                swap(chs, i, k);
                perm(list, chs, k + 1, len);
                swap(chs, i, k);
            }
        }

    }

    public static void swap(char[] chs, int i, int j) {
        char tem = chs[i];
        chs[i] = chs[j];
        chs[j] = tem;
    }

}
