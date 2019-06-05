package com.ztiany.basic.string;

/*
 正则表达式功能:
		1  匹配 String matches方法,用规则来匹配整个字符串
		2 切割
		3 替换   String $1 表示前一个字符串中的第一个组
		4  获取
  */

public class StudyRegular {

    public static void main(String args[]) {
        // splitDemo();
        String str = "jsldfdf9kasssss095dddddd4902,50290009458";
        String reg = "\\d{5,}";
        String newstr = "#";
        String reg2 = "(.)\\1+";
        String newstr2 = "$1";
        replace(str, reg2, newstr2);// 把叠词替换成单个字母
    }

    public static void test() {
        String str = "abc";
        String regex = "[abc]";// 表示 第一个 字符只能是 abc中的一个 并且该字符串只能有一个字符
        String regex1 = "[a-j][^d-l]";// 表示 第一个 字符只能是a-j中的一个 第二个字符除了d-l中一个都可以
        // 并且该字符串只能有两个个字符
        String regex2 = "[a-zA-Z][a-k[n-v]]";// 表示 第一个字符只要是 字符就可以 第一个字符 只能在 a-k
        // 或者 n-v中 只能有两个字符
        /*
         * [a-z&&[def]] d、e 或 f（交集） [a-z&&[^bc]] a 到 z，除了 b 和 c：[ad-z]（减去）
		 * [a-z&&[^m-p]] a 到 z，而非 m 到 p：[a-lq-z]（减去）
		 * 
		 * 
		 * 预定义字符： . 任何字符（与行结束符可能匹配也可能不匹配） \d 数字：[0-9] \D 非数字： [^0-9] \s 空白字符：[
		 * \t\n\x0B\f\r] \S 非空白字符：[^\s] \w 单词字符：[a-zA-Z_0-9] \W 非单词字符：[^\w]
		 */
        System.out.println(str.matches(regex));
    }

    public static void split() {
        String str = "abbjslllfjfajjjalfgd";
        String spReg = " +";// 按照多个空格切割字符串
        String reg = "\\.";// 表示 按照 .来切
        String reg1 = "\\\\";// 表示按照 \\ 来切
        String reg2 = "(.)\\1"; // 按照叠词完成切割 为了可以让规则的结果被重用
        // 可以将规则封装成一个组 组的出现都有编号 从1开始 想要使用已有的组可以通过 \n(n表示组的标号)的形式来获取
        // 这里+号表示可以出现多个叠词 如dddd
        String reg3 = "(.)\\1+";
        String[] arr = str.split("[a-c][b-n]");

        for (String s : arr) {
            System.out.println(s);
        }
    }

    private static void replace(String str, String reg, String newStr) {
        str = str.replaceAll(reg, newStr);
        System.out.println(str);
    }
}
