package me.ztiany.advance

/**
 *类型检查与转换：
 *
 * 类型检查：可以在运行时通过使用 is 操作符或其否定形式 !is 来检查对象是否符合给定类型
 *
 * 智能转换：在许多情况下，不需要在 Kotlin 中使用显式转换操作符，因为编译器跟踪 不可变值的 is-检查，并在需要时自动插入（安全的）转换
 *
 * 当编译器不能保证变量在检查和使用之间不可改变时，智能转换不能用。 更具体地，智能转换能否适用根据以下规则
 *
 *      val 局部变量——总是可以；
 *      val 属性——如果属性是 private 或 internal，或者该检查在声明属性的同一模块中执行。智能转换不适用于 open 的属性或者具有自定义 getter 的属性；
 *      var 局部变量——如果变量在检查和使用之间没有修改、并且没有在会修改它的 lambda 中捕获；
 *      var 属性——决不可能（因为该变量可以随时被其他代码修改）。
 *
 *      “不安全的”转换操作符：通常，如果转换是不可能的，转换操作符会抛出一个异常。因此，我们称之为不安全的。 Kotlin 中的不安全转换由中缀操作符 完成
 *      “安全的”（可空）转换操作符：为了避免抛出异常，可以使用安全转换操作符 as?，它可以在失败时返回 null
 */

private fun typeCheck() {
    //类型检擦
    var obj: Any? = "abc"
    if (obj is String) {
        println(obj.length)
    }

    //智能转换
    val x: Any = "abc"
    when (x) {
        is Int -> print(x + 1)
        is String -> print(x.length + 1)
        is IntArray -> print(x.sum())
    }

    //不安全的转换操作符
    var y = 1
    //类型转换
    val a: String = y as String
    val b: String? = y as? String?
}