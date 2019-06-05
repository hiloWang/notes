package com.ztiany.designpattern.decorator;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-01-10 22:50
 *         Email: ztiany3@gmail.com
 */
public class SumPrizeDecorator extends Decorator {

    public SumPrizeDecorator(Component component) {
        super(component);
    }

    @Override
    public float calcPrize(String user, int month) {
        float money = super.calcPrize(user, month);
        //计算总的销售奖金，这里为了方便，假设每个人的当月累计销售额都是100000元
        float prize = 100000 * 0.001F;
        System.out.println(user + " 累计奖金为 " + prize);
        return money + prize;
    }
}
