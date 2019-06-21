package com.ztiany.designpattern.visitor;

import java.util.Random;

/**
 * <pre>
 *      基础的员工类
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-02-18 19:51
 *         Email: ztiany3@gmail.com
 */
public abstract class Staff {

    static final Random RANDOM = new Random();

    private String name;
    private int kpi;

    public Staff(String name) {
        this.name = name;
        this.kpi = RANDOM.nextInt(10);
    }

    public abstract void accept(Visitor visitor);

    public int getKpi() {
        return kpi;
    }

    public String getName() {
        return name;
    }
}
