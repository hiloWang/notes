package me.ztiany.oop


/**
 * 定义类：Kotlin 中使用关键字 class 声明类。
 *
 * 1，类声明由类名、类头（指定其类型参数、主 构造函数等）和由大括号包围的类体构成。
 * 2，类头和类体都是可选的； 如果一个类没有类体，可以省略花括号。
 */
private open class Person constructor(name: String, var age: Int)

private fun testPerson() {
    val person = Person("abc", 21)
    person.age = 12//构造函数参数加上var或者val，则自动为类上的属性
}


/**
 * 构造函数：
 *
 * 1，在 Kotlin 中的一个类可以有一个主构造函数和一个或多个次构造函数。主构造函数是类头的一部分：它跟在类名（和可选的类型参数）后
 * 2，如果主构造函数没有任何注解或者可见性修饰符，可以省略这个 constructor 关键字。
 * 3，如果构造函数有注解或可见性修饰符，这个 constructor 关键字是必需的，并且这些修饰符在它前面
 * 4，主构造函数不能包含任何的代码。初始化的代码可以放 到以 init 关键字作为前缀的初始化块（initializer blocks）中
 * 5，主构造的参数可以在初始化块中使用。它们也可以在类体内声明的属性初始化器中使用
 */
private class Dog(name: String) {

    //属性初始化
    val tag = name.hashCode()

    //初始化块
    init {
        println(name)
    }

}

/**
 *次构造函数：类也可以声明前缀有 constructor 的次构造函数：
 *
 * 1，如果类有一个主构造函数，每个次构造函数需要委托给主构造函数，可以直接委托或者通过别的次构造函数间接委托。
 *      委托到同一个类的另一个构造函数 用 this 关键字即可
 * 2，如果一个非抽象类没有声明任何（主或次）构造函数，它会有一个生成的 不带参数的主构造函数。构造函数的可见性是 public。
 *      如果你不希望你的类 有一个公有构造函数，你需要声明一个带有非默认可见性的空的主构造函数
 * 3，在 JVM 上，如果主构造函数的所有的参数都有默认值，编译器会生成 一个额外的无参构造函数，它将使用默认值。
 */
private class Woman(val address: String, name: String, age: Int) : Person(name, age) {
    constructor(name: String) : this("china", name, 12)
    //constructor(name: String, age: Int) : super(name,age)// 如果类定义了主构造函数，这是不允许的，次构造函数必须调用主构造函数
}


/**
 *创建类的实例：要创建一个类的实例，就像普通函数一样调用构造函数， Kotlin 并没有 new 关键字。
 *
 *类可以包含：
 *
 *  1，构造函数和初始化块
 *  2，函数
 *  3，属性
 *  4，嵌套类和内部类
 *  5，对象声明
 */
private var person = Person("Ztiany", 21)
