package me.ztiany.bean.javaconfig;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.12 23:42
 */
public class BMWCar implements Car {

    @Override
    public void start() {
        System.out.println("宝马开动了");
    }

}
