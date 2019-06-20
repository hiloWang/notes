package me.ztiany.functions



/** 函数声明 */
private fun sum(x: Int, y: Int): Int {
    return x + y
}

/**
 * 中缀表示法：
 *
 * 在满足以下条件时,函数也可以通过中缀符号进行调用：它们是成员函数或者是扩展函数且只有一个参数，使用infix关键词进行标记，
 * 具体条件：**1，成员函数或扩展函数；2，只有一个参数；3，用`infix` 关键字标注**，
 * 应用场景： DSL
 */
private infix fun Int.pow(x: Int): Int {
    return Math.pow(this.toDouble(), x.toDouble()).toInt()
}

private fun testInfix() {
    val a = 3
    var b = 3.pow(5)
    //顾名思义，中缀表示法，pow以中缀的形式放在调用对象与参数之间，没有函数调用的形式。
    var c = 2 pow 3

    //这里step也是中缀表示法，而..是区间操作符
    for (i in 1..69 step 3) {

    }
}

/**
 * 默认参数：参数可以有默认值
 */
private fun read(b: Array<Byte>, off: Int = 0, len: Int = b.size) {

}

/**
 * 命名参数：传入参数时可以指定参数名。
 *
 * 调用 Java 函数时不能使用命名参数语法，因为 Java 字节码并不 总是保留函数参数的名称
 */
private fun reformat(str: String,
                     normalizeCase: Boolean = true,
                     upperCaseFirstLetter: Boolean = true,
                     divideByCamelHumps: Boolean = false,
                     wordSeparator: Char = ' ') {
}

private fun testReformat() {
    reformat("ABC", wordSeparator = 'c')
}


//返回 Unit 的函数：如果一个函数不返回任何有用的值，它的返回类型是 Unit。Unit 是一种只有一个值——Unit 的类型。这个 值不需要显式返回
fun printHello(name: String?): Unit {
    if (name != null)
        println("Hello ${name}")
    else
        println("Hi there!")
}


/**
 * 单表达式函数：
 *
 *      1，当函数返回单个表达式时，可以省略花括号并且在 = 符号之后指定代码体即可
 *      2，当返回值类型可由编译器推断时，显式声明返回类型是可选的
 *      3，具有块代码体的函数必须始终显式指定返回类型
 */
private fun add1(x: Int, y: Int): Int = x + y

private fun add2(x: Int, y: Int) = x + y


//可变数量的参数（Varargs）：函数的参数（通常是最后一个）可以用 vararg 修饰符标记
private fun <T> asList(vararg ts: T): List<T> {
    val result = ts // ts is an Array
            .toList()
    return result
}

private fun testAsList() {
    val a = arrayOf(1, 2, 3)
    //*a为伸展（spread）操作符（在数组前面加 *）
    val list = asList(-1, 0, *a, 4)
}


/**
 * 函数作用域：
 *      1， 局部函数
 *      2，成员函数
 *      3，顶层函数
 */

//顶层函数
private fun funType() {
    //局部函数
    fun inner() {

    }
}