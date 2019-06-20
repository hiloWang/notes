package com.ztiany.jspbase.i18n;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

//区域敏感：日期、时间、货币
public class MessageFormatDemo {

    Locale locale = Locale.US;

    public void test1() {

        //String s = "At 12:30 pm on jul 3,1998, a hurricance destroyed 99 houses and caused $1000000 of damage";
        Date d = new Date();
        int money = 1000000;
        DateFormat df1 = DateFormat.getTimeInstance(DateFormat.SHORT, locale);
        String time = df1.format(d);//12:30 pm

        DateFormat df2 = DateFormat.getDateInstance(DateFormat.DEFAULT, locale);
        String date = df2.format(d);//jul 3,1998

        NumberFormat nf = NumberFormat.getCurrencyInstance(locale);
        String smoney = nf.format(money);

        System.out.println("At " + time + "on " + date + ", a hurricance destroyed 99 houses and caused " + smoney + " of damage");
    }


    public void test2() {
        Date d = new Date();
        int money = 1000000;
        //占位符：对应着数组中元素的索引
        String pattern = "At {0} on {1}, a hurricance destroyed 99 houses and caused {2} of damage";
        MessageFormat mf = new MessageFormat(pattern);
        String s = mf.format(new Object[]{d, d, money});
        System.out.println(s);
    }


    public void test3() {
        Date d = new Date();
        int money = 1000000;
        //占位符：对应着数组中元素的索引
        String pattern = "At {0,time,short} on {1,date,medium}, a hurricance destroyed 99 houses and caused {2,number,currency} of damage";
        MessageFormat mf = new MessageFormat(pattern, locale);
        String s = mf.format(new Object[]{d, d, money});
        System.out.println(s);
    }
}
