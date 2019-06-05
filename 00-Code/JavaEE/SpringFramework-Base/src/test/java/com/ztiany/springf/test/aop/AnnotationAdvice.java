package com.ztiany.springf.test.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

//注解式通知类
@Aspect
public class AnnotationAdvice {

    @Pointcut("execution(* com.ztiany.springf.test.aop.AOPServiceImpl.*(..))")
    public void pc() {

    }

    //前置通知
    //指定该方法是前置通知,并制定切入点
    @Before("com.ztiany.springf.test.aop.AnnotationAdvice.pc()")
    public void before() {
        System.out.println("这是前置通知!!");
    }

    //后置通知
    @AfterReturning("execution(* com.ztiany.springf.test.aop.AOPServiceImpl.*(..))")
    public void afterReturning() {
        System.out.println("这是后置通知(如果出现异常不会调用)!!");
    }

    //环绕通知
    @Around("execution(* com.ztiany.springf.test.aop.AOPServiceImpl.*(..))")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("这是环绕通知之前的部分!!");
        Object proceed = pjp.proceed();//调用目标方法
        System.out.println("这是环绕通知之后的部分!!");
        return proceed;
    }

    //异常通知
    @AfterThrowing("execution(* com.ztiany.springf.test.aop.AOPServiceImpl.*(..))")
    public void afterException() {
        System.out.println("出事啦!出现异常了!!");
    }

    //后置通知
    @After("execution(* com.ztiany.springf.test.aop.AOPServiceImpl.*(..))")
    public void after() {
        System.out.println("这是后置通知(出现异常也会调用)!!");
    }

}
