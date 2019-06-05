package ztiany.chapter2

//--------------------
//Groovy的陷阱
//--------------------


//Groovy的 == 等于Java中的equals，
//如果希望比较引用是否相等，需要用Groovy中的is()方法
//但是事情情况并不总是如此，真实的情况是，当且仅当该类没有实现Comparable接口时，==操作才会映射到equals方法，否则映射到Comparable的compareTo方法

str1 = 'hello'
str2 = str1
str3 = new String('hello')
str4 = 'Hello'

println(' str1 == str2  ' + (str1 == str2))
println(' str1 == str3  ' + (str1 == str3))
println(' str1 == str4  ' + (str1 == str4))
println(' str1.is(str2) ' + (str1.is(str2)))
println(' str1.is(str3) ' + (str1.is(str3)))
println(' str1.is(str4) ' + (str1.is(str4)))


// 编译时类型检查默认关闭
//Groovy是类型可选的，Groovy编译器大多数情况下不会执行完整的类型检查，而是在遇到类型定义是执行强制类型转换，如果把一个类型
//赋值给一个Integer，变量：
Integer a = 31
//a = "ds"//可以正常编译通过，但是会在运行时进行类型转换 ，在Groovy中 x = y 在语义上等价于 x = (ClassOfX)y，即强转
//同样的，调用一个类不存在的方法，也不会在编译时期发生错误，但是会抛出MissingMethodException异常:
//a.noMethod()
//虽然这种编译器看上去并不严格，但事实上，对于动态编程和元编程来说，这是必要的，


// def in 都是groovy中的关键字，def用于定义方法和属性等，而in用于指定循环的区间，也不要在java代码中使用it变量，如果在闭包中使用了这个参数
//他引用的是闭包的参数


//在Groovy中，方法内不能有局部代码块
def functionA() {
//    {} 不能有这样的代码块，否则groovy以为是闭包
}



//闭包与匿名内部类冲突：闭包使用花括号，而匿名内部类也使用花括号，所以会造成冲突，如下一个类接受一个闭包参数

class Calibrator {
    Calibrator(calculationBlock) {
        println 'Calibrator created'
        calculationBlock()
    }
}

new Calibrator({
    println 'ddddd'
})
closesure = {
    println 'ccccc'
}

new Calibrator(closesure)


//分号总是可选的 ，至少有一个地方分号是必不可少的
class Semi {
    def val = 3;
    //这行代码如果不加分号，.将导致运行时MissingMethodException: No signature of method: java.lang.Integer.call()，不懂为什么是这个异常
    //其把下面构造代码块看成了一个闭包，
    {
        println "Semi created"
    }
}

println new Semi()
//如果使用静态代码块则不存在这样的问题，如果同时使用静态代码块和构造代码块，可以把构造代码块放在静态代码块的前面，从而避免分号。

//创建基本类型的数组语法不同
//在groovy中，创建数据使用以下方式:
int[] arr1 = [1, 2, 4]
def arr2 = [1, 2, 5] as int[]

println arr1.class.getName()//一般情况下，应该使用getClass方法，向Map、生成器等一些类对class属性有特殊的处理，为了避免出现意外，应使用getClass方法，
println arr2.class.getName()

































