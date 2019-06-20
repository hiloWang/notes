package com.ztiany.jspbase.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleDemo {

    public void test1() {
        //读默认的资源消息包：根据地区
        ResourceBundle rb = ResourceBundle.getBundle("com.ztiany.jspbase.resources.msg");
        String s = rb.getString("hello");
        System.out.println(s);
    }

    public void test2() {
        ResourceBundle rb = ResourceBundle.getBundle("com.ztiany.jspbase.resources.msg", Locale.US);
        String s = rb.getString("hello");
        System.out.println(s);
    }
}
