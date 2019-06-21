package me.ztiany.bean.javaconfig;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 18.6.12 23:44
 */
public class NormalDriver implements Driver {

    private Car mCar;

    @Override
    public void drive() {
        if (mCar != null) {
            mCar.start();
        } else {
            System.out.println("我没有车子");
        }
    }

    public void setCar(Car car) {
        mCar = car;
    }

}
