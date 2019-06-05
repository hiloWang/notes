package me.ztiany.oop

import java.util.*

/**
 * 属性和字段
 *
 * 声明属性：Kotlin的类可以有属性.属性可以用关键字var 声明为可变的，否则使用只读关键字val。
 *
 *       1，Getters 和 Setters：声明一个属性的完整语法包含：初始器（initializer）、getter 和 setter 都是可选的
 *            其初始器（initializer）、getter 和 setter 都是可选的。属性类型如果可以从初始器 （或者从其 getter 返回值，如下文所示）中推断出来，也可以省略
 *      2，一个只读属性的语法和一个可变的属性的语法有两方面的不同：1、只读属性的用 val开始代替var 2、只读属性不允许 setter
 *      3，Kotlin 1.1 起，如果可以从 getter 推断出属性类型，则可以省略它
 *      4，如果你需要改变一个访问器的可见性或者对其注解，但是不需要改变默认的实现， 你可以定义访问器而不定义其实现
 * */
private class Address {

    var country: String = ""
    var province: String = ""
    var city: String = ""
    var area: String = ""
    var default: Int = 1

    //给字段添加setter和getter
    var isDefault: Boolean
        get() = if (default == 1) {
            println("getter")
            true
        } else {
            println("getter")
            false
        }
        set(value) {
            println("setter")
            default = if (value) {
                1
            } else {
                0
            }
        }

    var setterVisibility: String = "abc"
        private set // 此 setter 是私有的并且有默认实现，所以他是只读属性
}

private fun testAddress() {
    val address = Address()
    println(address.isDefault)//访问的的时候，会调用isDefault的getter
    address.isDefault = true//赋值的时候会调用isDefault的setter
}


/**
 * 幕后字段：Kotlin 中类不能有字段。然而当使用自定义访问器时，有时有一个幕后字段（backing field）有时是必要的。
 *                 为此 Kotlin 提供 一个自动幕后字段，它可通过使用 field 标识符访问，
 *                 如果属性至少有一个访问器使用默认实现，或者自定义访问器通过 field 引用幕后字段，将会为该属性生成一个幕后字段。
 *
 * 幕后属性：如果你的需求不符合这套“隐式的幕后字段”方案，那么可以使用 幕后属性（backing property），即
 *
 */
private class BackingField {

    var counter = 0 // 此初始器值直接写入到幕后字段
        set(value) {
            if (value >= 0)
                field = value//field表示counter，field标识符只能用在属性的访问器内。
        }

    //既然字段和属性的差别主要是可见性，那么其实我们完全可以自己实现字段，也即幕后属性。
    //幕后属性：这里 _table 就是我们实现的“字段”，也叫幕后属性
    private var _table: Map<String, Int>? = null

    val table: Map<String, Int>
        get() {
            if (_table == null) {
                _table = HashMap() // 类型参数已推断出
            }
            return _table ?: throw AssertionError("Set to null by another thread")
        }
}


/**
 * 编译期常量：已知值的属性可以使用 const 修饰符标记为 编译期常量。 这些属性需要满足以下要求：
 *
 *1 ，位于顶层或者是 object 的一个成员
 *2 ，用 String 或原生类型 值初始化
 *3，没有自定义 getter
 */
private const val SUBSYSTEM_DEPRECATED: String = "This subsystem is deprecated"


/**
 * 延迟初始化属性：一般地属性声明为非空类型必须在构造函数中初始化。可以用 lateinit 修饰符标记该属性来实现延迟舒适化
 *
 *1，该修饰符只能用于在类体中（不是在主构造函数中）声明的 var 属性，并且仅当该属性没有自定义 getter 或 setter 时。该属性必须是非空类型，并且不能是原生类型。
 *2，在初始化前访问一个 lateinit 属性会抛出一个特定异常，该异常明确标识该属性 被访问及它没有初始化的事实。
 */
private class TestLateinit {
    //声明一个延迟初始化的属性
    lateinit var subject: String

    var custom: String
        get() {
            return "A"
        }
        set(value) {

        }
}

