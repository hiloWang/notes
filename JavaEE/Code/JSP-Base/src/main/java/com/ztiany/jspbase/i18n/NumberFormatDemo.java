package com.ztiany.jspbase.i18n;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;

public class NumberFormatDemo {

    public void test1() {
        int money = 1000000;
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        String s = nf.format(money);
        System.out.println(s);
    }

    public void test2() throws ParseException {
        String s = "$1,000,000.00";
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        Number n = nf.parse(s);
        System.out.println(n);
    }
}
