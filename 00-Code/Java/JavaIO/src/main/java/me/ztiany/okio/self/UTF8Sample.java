package me.ztiany.okio.self;

import okio.Utf8;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class UTF8Sample {
    
    public static void main(String... args){
        System.out.println(Utf8.size("湛添友"));
        System.out.println(Utf8.size("123"));
        System.out.println(Utf8.size("abc"));
        System.out.println(Utf8.size("中华人民共和国"));
        System.out.println(Utf8.size("美利坚合众国"));
    }
    
}
