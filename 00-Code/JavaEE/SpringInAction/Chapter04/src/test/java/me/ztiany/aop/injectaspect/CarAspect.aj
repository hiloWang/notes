package me.ztiany.aop.injectaspect;


import org.aspectj.lang.JoinPoint;

public aspect CarAspect {

    public CarAspect() {
    }

    private CartTrace cartTrace;

    public void setCartTrace(CartTrace cartTrace) {
        this.cartTrace = cartTrace;
    }

    pointcut startPoint():execution(* me.ztiany.aop.injectaspect.Car.start(..));

    before(JoinPoint joinPoint):startPoint(){
        Car car = (Car) joinPoint.getThis();
        cartTrace.addTrace(car);
    }

}
