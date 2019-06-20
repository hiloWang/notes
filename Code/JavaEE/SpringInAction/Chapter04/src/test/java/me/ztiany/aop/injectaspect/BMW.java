package me.ztiany.aop.injectaspect;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.18 22:32
 */
public class BMW implements Car {

    @Override
    public String getName() {
        return "BMW";
    }


    @Override
    public void start() {
        System.out.println("宝马发动");
    }

}
