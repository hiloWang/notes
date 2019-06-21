package ztiany.chapter5


//--------------------
//第五章：使用字符串
//--------------------
/*
Groovy中可以使用单引号 创建字符串，比如'hello',在java中'a'是一个char，"a"才是一个String，
但是在Groovy中并没有这样的区别，如果需要创建一个char，可以使用 a = 'a' as char
 */
println 'he said , "that is Groovy"'//双引号都可以直接放在字符串中，而不需要使用转义字符
str = 'string'
println str.getClass().name//java.lang.String 从输出看，''创建的字符串是java中的String，单引号的字符串，Groovy会直接转换成
//java中的String，而不会对其进行运算，如果需要运算，需要使用双引号字符串

val1 = 32
println "the val1 is $val1"//the val1 is 32 从输出看以看出Groovy对其进行了运算
//可以使用[]操作符来操作读取字符串中对应位置的字符串
println str[1]//但是不用修改，因为String是不可变对象

/*
使用双引号和//都可以创建字符串，双引号常用于定义字符串表达式，而/用于正则表达式
 */
val2 = 12;
println "he paid \$${val2} for that."//这里使用了\来转义字符$
//使用/则不需要转义
println(/he paid $$val2 for that. /)

//对于字符串中的$求值，简单的变量名和属性读取可以直接使用 $value方式，而如果是复杂的求值需要使用${表达式}






/*GString的惰性求值*/
what = new StringBuilder('fence')
text = "the cow jumped over the $what"
println  text
what.replace(0,5,"moon")//惰性求值
println text

//查看GString的真实实现
def printClassInfo(obj) {
    println "class : ${obj.getClass().name}"
    println "class: ${obj.getClass().superclass.name}"
}
val3 = 124;
printClassInfo("the stock closed at $val3")
printClassInfo(/the stock closed at $val3/)
printClassInfo("this is a String")
/*
class : org.codehaus.groovy.runtime.GStringImpl
class: groovy.lang.GString
class : org.codehaus.groovy.runtime.GStringImpl
class: groovy.lang.GString
class : java.lang.String
class: java.lang.Object

Groovy并不会简单的因为使用了双引号或者正斜杠就使用GString，而是会智能的选择
 */





//--------------------
//GString的惰性求值问题
//--------------------
//上面what = new StringBuilder('fence')的实例显得相当的合理，但是如果修改的是what的引用则情况就不一样的

price = 684.71
def company = 'google'
quote = "today $company stock closed at $price"

println quote

stocks = [Apple: 663.01, Microsoft:30.95]
stocks.each {
    key,value->
        company = key
        price = value
        println quote
}
/*
打印结果是：
today google stock closed at 684.71
today google stock closed at 684.71
today google stock closed at 684.71
可见，虽然改变了comany和price的引用，但是打印的语句却没有改变
可以改变comany和price的引用，却不能改变GString绑定的内容
 */

/*使用闭包来解决问题*/
companyClosure = {
    it.write(company)
}
priceClosure = {
    it.write("$price")
}
quote1 = "today ${companyClosure} stock closed at ${priceClosure}"
stocks.each {
    key,value ->
        company = key
        price = value
        println quote1
}
/*
today Apple stock closed at 663.01
today Microsoft stock closed at 30.95
原理是：但对GString进行求值时，如果其中包含一个变量，该变量会被简单的打印到Writer，通常是StringWriter，
如果GSting包含的是一个闭包，该闭包就会被调用，传入的是这个StringWriter,如果闭包不接受参数，则不传入参数，但是如果闭包接受的参数超过一个，就会抛出异常
 */

//去掉it参数，GString会使用闭包返回的参数
companyClosure = {->
     company
}
priceClosure = {->
    price
}
quote2 = "today ${companyClosure} stock closed at ${priceClosure}"
stocks.each {
    key,value ->
        company = key
        price = value
        println quote2
}
//更加简单的方法
quote3 = "today ${->company} stock closed at ${->price}"
stocks.each {
    key,value ->
        company = key
        price = value
        println quote3
}
























