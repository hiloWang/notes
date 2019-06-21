package com.ztiany.filter.utils;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class EncodeUtil {

    public static String base64Encode(String s) {
        return new BASE64Encoder().encode(s.getBytes());
    }

    public static String base64Decode(String s) {
        try {
            return new String(new BASE64Decoder().decodeBuffer(s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String md5(String s) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte b[] = md.digest(s.getBytes());
            return new BASE64Encoder().encode(b);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
