package ztiany.chapter4;

//--------------------
//闭包的委托
//--------------------
/*
Groovy中的闭包远远超出了简单的匿名方法，Groovy闭包支持方法委托，而且提供了方法分派的能力，这点和JavaScript对原型继承
的支持很像，

this、owner、delegate是闭包的三个属性，用于确定有哪个对象处理该闭包的方法调用，一般而言，delegate会设置为owner，但是对其加以修改
，可以挖掘出Groovy的一些非常好的元编程能力：
 */

def examiningClosure(closure) {
    closure.call()
}

examiningClosure(){
    println "in first closure:"
    println "class is "+getClass().name
    println "this is "+this+", super: "+this.getClass().superclass.name
    println "owner is "+owner+" , super: "+owner.getClass().superclass.name
    println "delegate is "+delegate+" , super: "+delegate.getClass().superclass.name


    examiningClosure(){
        println "in  closure    with the first closure:"
        println "class is "+getClass().name
        println "this is "+this+", super: "+this.getClass().superclass.name
        println "owner is "+owner+" , super :"+owner.getClass().superclass.name
        println "delegate is "+delegate+" , super: "+delegate.getClass().superclass.name
    }
}
/*
in first closure:
class is        ztiany.chapter4._006ClosureDelegate$_run_closure1
this is          ztiany.chapter4._006ClosureDelegate@149e0f5d, super: groovy.lang.Script
owner is       ztiany.chapter4._006ClosureDelegate@149e0f5d , super: groovy.lang.Script
delegate is   ztiany.chapter4._006ClosureDelegate@149e0f5d , super: groovy.lang.Script

in  closure    with the first closure:
class is                    ztiany.chapter4._006ClosureDelegate$_run_closure1_closure2
this is                      ztiany.chapter4._006ClosureDelegate@149e0f5d, super: groovy.lang.Script
owner is                   ztiany.chapter4._006ClosureDelegate$_run_closure1@10e92f8f , super :groovy.lang.Closure
delegate is               ztiany.chapter4._006ClosureDelegate$_run_closure1@10e92f8f , super: groovy.lang.Closure

上面打印了闭包的this、owner、delegate信息，由于第二个闭包是在第一个闭包中创建的，所以第一个闭包成了第二个闭包的owner
通过代码和打印信息说明：闭包被创建成了内部类，此外还说名，delegate被设置为owner，某些函数会修改闭包的delegate，比如with方法，闭包内
的this执行的是该闭包所绑定的对象(正在执行的上下文)，在闭包内引用的变量和方法都会绑定到this，他负责处理任何方法的调用，以及任何属性或变量的
访问，如果this无法处理，则转型owner，最后是delegate
 */


class Handler{
    def f1() {
        println "f1 of handler called......"
    }

    def f2() {
        println "f2 of handler called"
    }
}
class Example{
    def f1() {
        println "f1 of Example called......"
    }

    def f2() {
        println "f2 of Example called"
    }

    def foo(Closure closure){
        closure.delegate = new Handler()

        println "----------------------closure owner is  :"+closure.owner


        closure()
    }
}

new Example().foo(){

    println "class is "+getClass().name
    println "this is "+this+", super: "+this.getClass().superclass.name
    println "owner is "+owner+" , super :"+owner.getClass().superclass.name
    println "delegate is "+delegate+" , super: "+delegate.getClass().superclass.name

    f1()
    f2()
}

def f1(){
    println "f1 of script called......"
}
/*
class is ztiany.chapter4._006ClosureDelegate$_run_closure2
this is ztiany.chapter4._006ClosureDelegate@6ef888f6, super: groovy.lang.Script
owner is ztiany.chapter4._006ClosureDelegate@6ef888f6 , super :groovy.lang.Script
delegate is ztiany.chapter4.Handler@4b5d6a01 , super: java.lang.Object
f1 of script called......
f2 of handler called


可以看到，f1方法调用的是上下文的方法，因为this指向的是上下文，与owner一样，而上下文方法没有f2方法，所以调用的是delegate:Handler的f2方法
我们无法修改闭包的owner属性，        closure.owner = new Example()并没有效果。
 */


/*
一般情况是有效的，可能闭包做了特殊处理吧
 */
class Test{
    private int a;

}
def test = new Test(a:3)
test.a = 21
println test.dump()


/*
设置闭包的delegate属性可能会导致副作用，尤其是闭包还用与其他函数和线程时，如果完全肯定该闭包不会用在别的地方，那自然可以设置delegate，如果闭包用在了
其他地方，则可以复制一个闭包，在副本上设置delegate，：
 */
def closure = {
    f1()
    f2()
}
def clone = closure.clone()
new Example().foo {clone}

//还可以更优化，加载with方法剋一次性完成这三个动作
new Example().with {

}






