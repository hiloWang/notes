package me.ztiany.aop.javaconfig;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclareParents;
import org.springframework.stereotype.Service;

/*
通过注解引入新功能：利用被称为引入的AOP概念，切面可以为Spring bean添加新方法。

我们需要有一种方式将EnhanceInterface接口应用到BusinessService实现中。我们现在假设你能够访问BusinessService的所有实现，并对其进行修改，
让它们都实现EnhanceInterface接口。但是，从设计的角度来看，这并不是最好的做法，并不是所有的BusinessService都是具有EnhanceInterface特性的。
另外一方面，有可能无法修改所有的BusinessService实现，当使用第三方实现并且没有源码的时候更是如此。值得庆幸的是，借助于AOP的引入功能，
我们可以不必在设计上妥协或者侵入性地改变现有的实现。

和其他的切面一样，我们需要在Spring应用中将EnhanceInterfaceAspectBean声明为一个bean，Spring的自动代理机制将会获取到它的声明，
当Spring发现一个bean使用了@Aspect注解时，Spring就会创建一个代理，然后将调用委托给被代理的bean或被引入的实现，
这取决于调用的方法属于被代理的bean还是属于被引入的接口。
 */
@Service
@Aspect
public class EnhanceInterfaceAspectBean {

    /*
    value属性指定了哪种类型的bean要引入该接口。在本例中，也就是所有实现Performance的类型。
    （标记符后面的加号表示是Performance的所有子类型，而不是Performance本身。）

    defaultImpl属性指定了为引入功能提供实现的类。在这里，我们指定的是DefaultEncoreable提供实现。

    @DeclareParents注解所标注的静态属性指明了要引入了接口。在这里，我们所引入的是EnhanceInterface接口。
     */
    @DeclareParents(
            value = "me.ztiany.aop.javaconfig.BusinessService+",
            defaultImpl = EnhanceInterfaceImpl.class
    )
    private static EnhanceInterface mEnhanceInterface;

}
