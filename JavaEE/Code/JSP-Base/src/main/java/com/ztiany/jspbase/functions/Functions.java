package com.ztiany.jspbase.functions;

/**
 * 自定义EL函数
 *
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.4.21 16:35
 */
public class Functions {

    public static String change(String source) {
        if (source == null || "".equals(source)) {
            return "";
        }

        if (source.length() == 1) {
            return source.toLowerCase();
        }

        char[] chars = source.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (i % 2 == 0) {
                sb.append(Character.toLowerCase(chars[i]));
            } else {
                sb.append(Character.toUpperCase(chars[i]));
            }
        }
        return sb.toString();
    }

}
