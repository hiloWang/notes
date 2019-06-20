package com.ztiany.register.utils;

import java.security.MessageDigest;

import sun.misc.BASE64Encoder;

public class MD5Util {

    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");//指定加密算法
            byte b[] = md.digest(str.getBytes());//不一定对应着字符串
            return new BASE64Encoder().encode(b);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
