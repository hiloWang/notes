package com.ztiany.designpattern.decorator;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-01-10 22:52
 *         Email: ztiany3@gmail.com
 */
public class GroupPrizeDecorator extends Decorator {

    public GroupPrizeDecorator(Component component) {
        super(component);
    }

    @Override
    public float calcPrize(String user, int month) {
        float money = super.calcPrize(user, month);
        //计算团队奖金
        final float[] total = {0};
        DB.monthSaleMoney.entrySet().forEach(stringFloatEntry -> {
            total[0] += stringFloatEntry.getValue();
        });

        float prize = total[0] * 0.01F;
        System.out.println(user + " 当月的团队奖金为 " + prize);
        return money + prize;
    }
}
