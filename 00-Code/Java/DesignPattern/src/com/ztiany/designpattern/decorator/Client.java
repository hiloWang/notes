package com.ztiany.designpattern.decorator;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-01-10 22:55
 *         Email: ztiany3@gmail.com
 */
public class Client {

    public static void main(String... args) {
        //1 基本的计算奖金的类
        Component component = new ConcreteComponent();
        //2 根据需求，创建具体的装饰器
        Decorator monthDecorator = new MonthPrizeDecorator(component);
        Decorator sumDecorator = new SumPrizeDecorator(monthDecorator);
        //张三和李四是普通员工，计算当月奖金和当月累计奖金即可。
        System.out.println("=============张三的应得奖金为："+sumDecorator.calcPrize("张三", 1));
        System.out.println("=============李四的应得奖金为：："+sumDecorator.calcPrize("李四", 1));
        //假设王五是经理，还需要计算团队奖金
        Decorator groupDecorator = new GroupPrizeDecorator(sumDecorator);
        System.out.println("=============王五的应得奖金为：："+groupDecorator.calcPrize("王五", 1));
    }
}
