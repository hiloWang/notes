package com.ztiany.jspbase.i18n;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormatDemo {

    private Locale locale = Locale.CHINA;

    //2014年6月24日 星期二 下午03时37分00秒 CST
    public void test1() {
        Date d = new Date();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.FULL, DateFormat.FULL, locale);
        String s = df.format(d);
        System.out.println(s);
    }

    //2014年6月24日 下午03时40分47秒
    public void test2() {
        Date d = new Date();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
        String s = df.format(d);
        System.out.println(s);
    }

    //2014-6-24 15:41:21
    public void test3() {
        Date d = new Date();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, locale);
        String s = df.format(d);
        System.out.println(s);
    }

    //14-6-24 下午3:41
    public void test4() {
        Date d = new Date();
        DateFormat df = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale);
        String s = df.format(d);
        System.out.println(s);
    }

    public void test5() throws ParseException {
        String s = "2014-06-24 15:43:48";
        //DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date d = df.parse(s);
        System.out.println(d);
    }
}
