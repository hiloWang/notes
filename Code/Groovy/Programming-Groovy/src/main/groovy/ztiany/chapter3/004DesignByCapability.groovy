package ztiany.chapter3

//--------------------
//能力式设计
//--------------------
/*
Java中的面向接口编程：基于接口编程尽管非常强大，但是往往有很多限制(好吧，至少需要实现接口，有的程序员甚至刚开始没有设计接口)

实例：搬动一些重物，需要找一个愿意帮忙且有能力帮忙的人，在Java中有这样的演变
 */

class Man {
    void moveThings() {
        println 'man move things'
    }
}

public void takeHelp(Man man) {
    man.moveThings()
}
//但是如果有一个女人也可以帮忙了呢。需要把moveThings设计为抽象的能力
abstract class Human {
    abstract void moveThings()
}
class Woman extends Human{
    @Override
    void moveThings() {

    }
}

//但是如果大象也可以帮忙呢？最后还是选择了吧moveThing抽象到接口中(事实上确实应该如此，这本来就只是一种能力，也许这并不是一个恰当的例子)
interface Helper{
    void moveThings()
}
//至此扩展需要付出很多的努力。要使用多种多样的对象，需要创建接口，并修改依赖接口的代码



/*
看看Groovy的动态类型能力如果应对上面的例子：
 */
def takeHelpGroovy(helper) {
    helper.moveThings()
}
/*
takeHelpGroovy方法接受一个helper，并且没有指定其类型，这样它的默认类型就是Object，然后调用了其moveThings方法，这就是
能力是设计，我们利用了对象的能力——依赖一个隐式的接口，这种称为鸭子模型：如果它走起来像鸭子，叫起来也像鸭子，那么它就是鸭子
想要这种能力只需要实现该方法，而不需要实现或扩展任何东西，这样的接口就是少了许多繁文缛节，增加了生产效率
 */

takeHelpGroovy(new Man())
//因为来自java的背景，可能需要付出更多的努力才能习惯groovy的动态特性，但是一旦感觉对了，就可以好好利用了。




/*
使用动态类型需要自律
 */

/*

当我们看到了动态类习惯的诸多优化与灵活性时，也应该看到其风险

- 在没有创建helper类时，可能会敲错名字：依赖单元测试，使用动态类型语言而没有做单元测试就像是在玩火
- 没有类型信息，怎么知道给方法什么呢？：良好的命名约定
- 如果把方法发给一个不能提供搬动重物的事物(没有对应方法)，又会怎样：当对象不支持预期的方法时，Groovy会抛出异常，同时可以使用Groovy提供的respondTo方法来判断对象
是否有对应的能力(方法)
 */

class RespondToTest{
    String doSomething(String str) {
        str.toUpperCase()
    }
}

def rt = new RespondToTest()

println rt.metaClass.respondsTo(rt, 'doSomething')//[public java.lang.String ztiany.chapter3.RespondToTest.doSomthing(java.lang.String)] 分别是返回值，方法，参数
if (rt.metaClass.respondsTo(rt, 'doSomething')) {
    println rt.doSomething("dd")

}




//--------------------
//可选类型
//--------------------
/*
Groovy是可选类型的，这意味着可以不指定任何类型让Groovy来确定，也可以将精确的指定所要使用的变量或引用的类型
需要注意的是，Groovy默认并不做完整的类型检查，当我们写下X obj = 2时，Groovy所做的只是简单的做一个强转，如：X obj = (X)2;


 */

























