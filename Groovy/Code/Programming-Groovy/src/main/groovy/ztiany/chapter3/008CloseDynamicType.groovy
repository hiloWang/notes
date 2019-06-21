package ztiany.chapter3

import groovy.transform.TypeChecked
import groovy.transform.TypeCheckingMode

//--------------------
//关闭动态类型
//--------------------
/*
Groovy的元编程能力，都依赖于其动态类型，但动态类型也是有代价的，原本编译时期可以发现的问题被推迟到运行时，然后动态方法分配机制也是有开销的，

可以让Groovy编译器将其类型检查从动态的不严格模式收紧到我们对静态类型编译器所期待的水平，还可以权衡动态类型和元编程能力的收益，让Groovy编译器静态
编译代码，一遍获取更高效的字节码
 */

/*
静态类型检查
@TypeChecked注解可以让Groovy进行静态编译检查，此注解可以作用于类或者方法上
 */

@TypeChecked
def shout(String str) {
    println 'printing in uppercase'
    println str.toUpperCase()
    println 'printing again uppercase'
    println str.toUpperCase()//toUppercase方法拼写错误，这个方法没有使用任何元编程，因此可以利用编译时验证
    str.reverse()

}

shout 'abcd'

/*
@TypeChecked注解在编译时期会验证方法或者属性是或属于某一个类，这会阻止我们使用元编程能力。

静态类型会限制使用动态方法，然而他并没有阻止使用Groovy行JDK中的类添加方法，
静态类型检查器会检查这些类中的方法和属性，并且还会检查一个特殊的DefaultGroovyMethods类，其中包含了一些
有用的，优雅的扩展方法，此外它还会检查开发者能够添加的定制扩展(后面将会学习)，可以查看DefaultGroovyMethodsSupport类及其扩展
 */

//DefaultGroovyMethods中的方法，例如println
//开发者能够添加的定制扩展 ,例如    str.reverse(),来自StringGroovyMethods

//instanceof不需要强转
@TypeChecked
def use(Object o) {
    if (o instanceof String) {
        o.length()
    } else {
        0
    }
}

println use('你啊')
println use(221)

//TypeChecked忽略一些方法检查
@TypeChecked
class Sample {

    def method() {

    }

    //DateGroovyMethods中包含toCalendar方法，所以可以编译通过
    def dateTest(Date date) {
        date.toCalendar()
    }


    @TypeChecked(TypeCheckingMode.SKIP)//使用此注解，则会跳过静态类型检查
    def method2(String str) {
        str.shot()
    }
}

