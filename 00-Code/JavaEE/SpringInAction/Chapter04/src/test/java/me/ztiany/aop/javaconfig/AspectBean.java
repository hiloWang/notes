package me.ztiany.aop.javaconfig;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Service;

import java.util.Arrays;


@Aspect
@Service
public class AspectBean {

    /*
    1：execution 表示当方法执行时
    2：* 表示任意返回值
    3：.. 表示任意参数
    4：&&用于连接其他条件
    5：within表示仅匹配给定的返回(这里是可选的)
    6：&&表示（and）关系、||表示（or）关系、!表示（not）操作，因为&在 XML 中有特殊含义， 所以在 Spring 的 XML 配置里面描述切点时，
          可以使用 and 来代替&&。同样，or 和 not 可以分别用来代替||和!。
     */
    @Before("execution(* me.ztiany.aop.javaconfig.BusinessServiceImpl.addProduct(..)) && within(me.ztiany.aop.javaconfig.*)")
    public void beforeAddProduct(JoinPoint joinPoint) {
        System.out.println("addProduct called with: " + joinPoint.getThis());
    }


    /*
    bean表示该Advance只应用于对应id的bean
     */
    @After("execution(* me.ztiany.aop.javaconfig.BusinessServiceImpl.addProduct(..)) && bean(business)")
    public void afterAddProduct(JoinPoint joinPoint) {
        System.out.println("addProduct called with: " + joinPoint.getThis());
    }

    /*定义一个切入点*/
    @Pointcut("execution(* me.ztiany.aop.javaconfig.BusinessServiceImpl.set*(..))")
    public void setProductPointcut() {
    }

    @Around("setProductPointcut()")
    public Object processSetProduct(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            System.out.println(proceedingJoinPoint.getSignature() + " called with: " + Arrays.toString(proceedingJoinPoint.getArgs()));
            return proceedingJoinPoint.proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    /*定义一个切入点：args(productId)表明传递给deleteProduct()方法的int类型参数也会传递到通知中去*/
    @Pointcut("execution(* me.ztiany.aop.javaconfig.BusinessServiceImpl.deleteProduct(int )) && args(productId)")
    public void deleteProductPointcut(int productId/*陛下与上面args中指定的参数一致*/) {
    }

    /* 直接获取被拦截方法的参数： */
    @Before(value = "deleteProductPointcut(productId)", argNames = "productId")
    public void beforeDeleteProduct(int productId) {
        System.out.println("deleteProduct called with productId: " + productId);

    }

}
