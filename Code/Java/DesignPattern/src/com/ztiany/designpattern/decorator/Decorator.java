package com.ztiany.designpattern.decorator;

/**
 * <pre>
 *      定义抽象装饰器，需要和被装饰对象实现同样的接口
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-01-10 22:43
 *         Email: ztiany3@gmail.com
 */
public abstract class Decorator extends Component {

    private Component mComponent;

    public Decorator(Component component) {
        mComponent = component;
    }

    @Override
    public float calcPrize(String user, int month) {
        return mComponent.calcPrize(user, month);
    }

}
