package com.ztiany.designpattern.decorator;

import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-01-10 22:46
 *         Email: ztiany3@gmail.com
 */
public class DB {

    public static final Map<String, Float> monthSaleMoney = new HashMap<>();

    static {
        monthSaleMoney.put("张三", 100F);
        monthSaleMoney.put("李四", 200F);
        monthSaleMoney.put("王五", 300F);
    }
}
