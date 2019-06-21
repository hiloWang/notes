package ztiany.chapter0

//--------------------
// 引言，Groovy特性粗略介绍
//--------------------

//动态语言实例，Integer运行时添加方法
Integer.metaClass.percentRaise = {
    amount ->
        amount * (1 + delegate / 100.0)//delegate 引用的是integer的一个实例，默认情况下闭包在那个类创建，它的delegate就是类的对象
}

println 5.percentRaise(8000)


//2 Groovy的for循环
3.times {
    println(it)
}


//在使用groovy编程时，java有的groovy几乎都有，groovy类同样的也扩展了Object类，
//Groovy类就是java类，面向对象规范和java语义都保留了下来
println(XmlParser.class)
println(XmlParser.class.superclass)


//Groovy是动态的，类型也是可选的,下面给String添加了一个方法用于检测器是否为回文结构，
// 这样不浸入一个类的源代码，即可使用便捷的方法来轻松的扩展类，即使是不可变的String
String.metaClass.isPalindrome = {
        delegate == delegate.reverse()
}

println "abc".isPalindrome()
println "aba".isPalindrome()


//Groovy使用便捷的方法和闭包扩展了JDK,例如下面list的join方法，用于把元素连接起来
list = ["I", "am", "Ztiany"]
println list.join("   ")












