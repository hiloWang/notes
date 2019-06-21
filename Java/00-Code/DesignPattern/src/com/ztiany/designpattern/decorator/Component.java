package com.ztiany.designpattern.decorator;

/**
 * <pre>
 *      计算奖金的组件接口
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-01-10 22:40
 *         Email: ztiany3@gmail.com
 */
public abstract class Component {
    public abstract float calcPrize(String user, int month);
}

