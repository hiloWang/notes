package com.ztiany.designpattern.decorator;

/**
 * <pre>
 *      基本的实现奖金的类，也就是被装饰器装饰的对象
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-01-10 22:42
 *         Email: ztiany3@gmail.com
 */
public class ConcreteComponent extends Component {


    @Override
    public float calcPrize(String user, int month) {
        return 0;
    }
}
