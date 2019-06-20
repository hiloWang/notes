package me.ztiany.extend

import kotlin.reflect.KProperty


/**
 *Kotlin扩展：Kotlin 同 C# 类似，能够扩展一个类的新功能而无需继承该类或使用像装饰者这样的任何类型的设计模式。这通过叫做扩展的特殊声明完成。
 *                  Kotlin 支持扩展函数 和 扩展属性。
 *
 * 扩展的作用域：要使用所定义包之外的一个扩展，我们需要在调用方导入它
 */

//扩展函数示例
private fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    //这个 this 关键字在扩展函数内部对应到接收者对象
    val temp = this[index1]
    this[index1] = this[index2]
    this[index2] = temp
}

private fun testSwap() {
    val list = mutableListOf(1, 2, 3, 4, 5, 6, 7)
    list.swap(1, 5)
    println(list)
}

private fun intExtend() {
    //扩展的两种方式：

    //1：
    fun Int.add(value: Int): Int {
        return this + value
    }
    println(5.add(5))

    //2：
    val subtract = fun Int.(value: Int): Int {
        return this - value
    }
    println(10.subtract(20))
}

/**
 * 扩展是静态的：扩展不能真正的修改他们所扩展的类。通过定义一个扩展，你并没有在一个类中插入新成员， 仅仅是可以通过该类型的变量用点表达式去调用这个新函数。
 *
 *       1，扩展函数是静态分发的，即他们不是根据接收者类型的虚方法。 这意味着调用的扩展函数是由函数调用所在的表达式的类型来决定的，而不是由表达式运行时求值结果决定的
 *      2，如果一个类定义有一个成员函数和一个扩展函数，而这两个函数又有相同的接收者类型、相同的名字 并且都适用给定的参数，这种情况总是取成员函数。
 */
private open class ExtendC

private class ExtendD : ExtendC()

private fun ExtendC.foo() = "c"

private fun ExtendD.foo() = "d"

private fun printFoo(c: ExtendC) {
    println(c.foo())
}


/**
 * 可空接收者：注意可以为可空的接收者类型定义扩展。这样的扩展可以在对象变量上调用， 即使其值为 null，并且可以在函数体内检测 this == null，
 *                    这能让你 在没有检测 null 的时候调用 Kotlin 中的toString()：检测发生在扩展函数的内部
 */
private fun Any?.toString(): String {
    if (this == null) return "null"
    // 空检测之后，“this”会自动转换为非空类型，所以下面的 toString()
    // 解析为 Any 类的成员函数
    return this.toString()
}


/**
 * 扩展属性：和函数类似，Kotlin 支持扩展属性
 *
 *          由于扩展没有实际的将成员插入类中，因此对扩展属性来说幕后字段是无效的。这就是为什么扩展属性不能有初始化器。
 *          他们的行为只能由显式提供的 getters/setters 定义
 */
private val <T> List<T>.lastIndex: Int
    get() = size - 1//扩展属性只有setter


/**
 * 扩展声明为成员：在一个类内部可以为另一个类声明扩展。在这样的扩展内部，有多个 隐式接收者 —— 其中的对象成员可以无需通过限定符访问。
 *
 * 1，扩展声明所在的类的实例称为 分发接收者
 * 2，扩展方法调用所在的接收者类型的实例称为 扩展接收者
 * 3，对于分发接收者和扩展接收者的成员名字冲突的情况，扩展接收者 优先。要引用分发接收者的成员你可以使用 限定的 this 语法。
 */
private class ExtendE {
    fun bar() {
        println("d bar code")
    }
}

private class ExtendF {

    fun baz() {
        println("f bar code")
    }

    fun ExtendE.foo() {
        bar()   // 调用 D.bar
        baz()   // 调用 C.baz
        toString()//优先选择ExtendE的
        this@ExtendF.toString()//this语句调用ExtendF的
    }

    override fun toString(): String {
        println("to string called")
        return super.toString()
    }

    fun caller(e: ExtendE) {
        e.foo()   // 调用扩展函数
    }
}

private fun <T> T?.doIfNull(action: () -> Unit) {
    if (this == null) {
        action()
    }
}

private fun testDoIfNull() {
    val string: String? = null
    println(string ?: "aa")
    string.doIfNull {
        println("abc")
    }
}


private class Delegate {

    private lateinit var name: String

    operator fun getValue(thisRef: Int?, property: KProperty<*>): String {
        if (!::name.isInitialized) {
            name = "i am int"
        }
        return name
    }

}

val Int.name: String  by Delegate()

private fun test() {
    //给Int添加
    val sum = fun Int.(other: Int): Int = this + other
    1.sum(2)
    //给String添加
    val add = fun String.(o: String): String = this + o
    fun testAdd() {
        val result = add.invoke("A", "B")
        println(result)//"AB"
    }
    "A".add("B")
}

fun main(args: Array<String>) {
    testSwap()
    printFoo(ExtendD())//结果是：c
    val c: ExtendC? = null
    println(c.toString())
    val list = listOf(3, 3)
    println(list.lastIndex)
    testDoIfNull()

    println(1.name)
}