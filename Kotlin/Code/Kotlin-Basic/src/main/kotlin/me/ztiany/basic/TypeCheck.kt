package me.ztiany.basic


/**
类型检查与转换：

    is 运算符检测一个表达式是否某类型的一个实例。如果一个不可变的局部变量或属性已经判断出为某类型，
    那么检测后的分支中可以直接当作该类型使用，无需显式转换
 */
private fun getStringLength(obj: Any): Int? {
    if (obj is String) {
        // `obj` 在该条件分支内自动转换成 `String`
        return obj.length
    }
    // 在离开类型检测分支后，`obj` 仍然是 `Any` 类型
    return null
}

private fun getStringLength_1(obj: Any): Int? {
    if (obj !is String) return null
    // `obj` 在这一分支自动转换为 `String`
    return obj.length
}

private fun getStringLength_2(obj: Any): Int? {
    if (obj is String && obj.length > 0) {
        // `obj` 在 `&&` 右边自动转换成 `String` 类型
        return obj.length
    }
    return null
}

fun main(args: Array<String>) {
    //内部函数
    fun printLength(obj: Any) {
        println("'$obj' string length is ${me.ztiany.basic.getStringLength(obj) ?: "... err, not a string"} ")
    }
    printLength("i am ztiany")
    printLength(1000)
    printLength(listOf(Any()))
}

