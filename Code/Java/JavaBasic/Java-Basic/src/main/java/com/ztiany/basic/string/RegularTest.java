package com.ztiany.basic.string;

import java.util.Collections;
import java.util.TreeSet;


public class RegularTest {

    public static void main(String args[]) {
        url();
    }

    private static void url() {
        // 对域名进行校验
        String url = "zhantianyou19001005@163.com";
        String regex = "\\w+@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+){1,3}";// 比较精确的匹配
        System.out.println(url.matches(regex));
    }

    private static void test() {
        String str = "我....我..要..要...要....学学....学...学...编编.....编....程程......程";
        // 思路 将已有的字符串变成另一个字符串 使用 替换
        // 1可以先将 .去掉 2 将多个重复的内容变成单个内容
        String reg = "(\\.)\\1+";
        str = str.replaceAll(reg, "");
        String reg2 = "(.)\\1+";
        str = str.replaceAll(reg2, "$1");
        System.out.println(str);
    }

    private static void ipSort() {
        // 将下面的IP地址进行地段顺序排序
        String ip = "192.168.1.1 192.65.12.1 169.253.123.11 1.56.66.45 127.0.0.1";
        // 还按照字符串的自然顺序进行排序 只要让他们每一段都是三位即可
        // 思路 1 按照每一段需要的最多0进行补位 那么每一段至少保证有三位
        // 2 将每一段只保留 三位 这样所有的iP地址都是每一段三位
        ip = ip.replaceAll("(\\d+)", "00$1");
        System.out.println(ip);
        ip = ip.replaceAll("0*(\\d{3})", "$1");
        System.out.println(ip);
        String arr[] = ip.split(" ");
        TreeSet<String> set = new TreeSet<>();
        Collections.addAll(set, arr);
        for (String s : set) {
            System.out.println(s.replaceAll("0*([0-9]{1,})", "$1"));
        }
    }
}
