package com.ztiany.designpattern.decorator;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-01-10 22:45
 *         Email: ztiany3@gmail.com
 */
public class MonthPrizeDecorator extends Decorator {

    public MonthPrizeDecorator(Component component) {
        super(component);
    }

    @Override
    public float calcPrize(String user, int month) {
        //计算前面的奖金
        float money = super.calcPrize(user, month);
        //然后计算当月业务奖金 ，
        float prize = DB.monthSaleMoney.get(user) * 0.03F;
        System.out.println(user + " 当月的奖金为 " + prize);
        return money + prize;
    }
}
