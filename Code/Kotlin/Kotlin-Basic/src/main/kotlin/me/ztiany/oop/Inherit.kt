package me.ztiany.oop


/**
 * 继承：
 *
 * 1，在 Kotlin 中所有类都有一个共同的超类 Any，这对于没有超类型声明的类是默认超类
 * 2，Any 不是 java.lang.Object；尤其是，它除了 equals()、hashCode()和toString()外没有任何成员
 * 3，要声明一个显式的超类型，需要把类型放到类头的冒号之后
 * 4，如果基类有一个主构造函数，其基类型必须用基类型的主构造函数参数就地初始化。
 * 5，如果基类没有主构造函数，那么每个次构造函数必须 使用 super 关键字初始化其基类型，或委托给另一个构造函数做到这一点。
 *       注意，在这种情况下，不同的次构造函数可以调用基类型的不同的构造函数
 * 6，类上的 open 标注与 Java 中 final 相反，它允许其他类 从这个类继承。默认情况下，在 Kotlin 中所有的类都是 final
 */


private open class Base//Base继承与Any

//由主构造函数，使用this
private class Derived(p: Int) : Base() {
    constructor(p: Int, name: String) : this(p)
}

//没有主构造函数，用super
private class Derived2 : Base {
    constructor(p: Int, name: String) : super() {
        println(name)
    }
}


/**
 * 覆盖方法和属性 ：
 *
 *1，   Kotlin 需要显式 标注可覆盖的成员（称之为开放）和覆盖后的成员
 *2，  子类的函数上必须加上 override标注。如果没写，编译器将会报错
 *3，  如果函数没有标注 open，则子类中不允许定义相同签名的函数， 不论加不加 override。
 *4，  在一个 final 类中（没有用 open 标注的类），开放成员是禁止的
 *5，  属性覆盖与方法覆盖类似：在超类中声明然后在派生类中重新声明的属性必须以 override 开头，并且它们必须具有兼容的类型。
 *       每个声明的属性可以由具有初始化器的属性或者具有 getter 方法的属性覆盖。
 *6， 可以用一个 var 属性覆盖一个 val 属性，但反之则不行。这是允许的，因为一个 val 属性本质上声明了一个 getter 方法，
 *      而将其覆盖为 var 只是在子类中额外声明一个 setter 方法
 *7，可以在主构造函数中使用 override 关键字作为属性声明的一部分。
 * */

private open class Bird(name: String, age: Int) {
    open fun fly() {}
    var mAge: Int = age
    open val mId = name.hashCode()
    open var color: Int = 0xFF0000
}

private class Goose(name: String, age: Int, override var color: Int) : Bird(name, age) {
    val mTag = "Goose"
    override fun fly() {}
    override var mId = (mTag + name).hashCode()
}

private fun testOverride(args: Array<String>) {
    val goose = Goose("aa", 21, 0xFF00FF)
    println(goose.mTag)
    println(goose.mAge)
    println(goose.mId)
    println(goose.color)
}

/**
 * 覆盖规则： 如果一个类从它的直接超类继承相同成员的多个实现，它必须覆盖这个成员并提供其自己的实现（也许用继承来的其中之一）。
 *                  为了表示采用从哪个超类型继承的实现，我们使用由尖括号中超类型名限定的 super，如 super<Base>
 */

private open class A {

    open fun f() {
        print("A")
    }

    fun a() {
        print("a")
    }
}

private interface B {

    fun f() {
        print("B")
    } // 接口成员默认就是“open”的

    fun b() {
        print("b")
    }
}

private class C() : A(), B {
    // 编译器要求覆盖 f()：
    override fun f() {
        super<A>.f() // 调用 A.f()
        super<B>.f() // 调用 B.f()
    }
}
