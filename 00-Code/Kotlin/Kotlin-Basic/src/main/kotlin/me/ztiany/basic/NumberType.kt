package me.ztiany.basic


/**
定义数字：在 Kotlin 中，所有东西都是对象，在这个意义上讲所以我们可以在任何变量上调用成员函数和属性。
对于数字没有隐式拓宽转换，在 Kotlin 中字符不是数字。
 */
fun defineNumber() {

    //Kotlin 提供以下数字类型
    var d: Double = 33.33
    var f: Float = 32.3F
    var l: Long = 330000L
    var i: Int = 32
    var s: Short = 32
    var b: Byte = 32

    //字面常量,不支持八进制
    var decima: Int = 213 // 十进制
    var binary: Int = 0b0000101//二进制
    var hex: Int = 0x0F//十六进制

    //数字字面值中的下划线(1.1起)
    val oneMillion = 1_000_000
    val creditCardNumber = 1234_5678_9012_3456L
    val hexBytes = 0xFF_EC_DE_5E
    val bytes = 0b11010010_01101001_10010100_10010010

    /*浮点可以除0*/
    val h = 3 / 0.0
    val j = 3.0 / 0.0
}


/** 表示方式 :在 Java 平台数字是物理存储为 JVM 的原生类型，除非我们需要一个可空的引用（如 Int?）或泛型。后者情况下会把数字装箱。*/
fun numberBox() {
    // ===：数字装箱不必保留同一性
    val a: Int = 10000
    print(a === a) // 输出“true”
    val boxedA: Int? = a
    val anotherBoxedA: Int? = a
    print(boxedA === anotherBoxedA) // ！！！输出“false”！！！

    // ==：但保留了相等性
    print(a == a) // 输出“true”
    print(boxedA == anotherBoxedA) // 输出“true”
}


/**
 * 显式转换：由于不同的表示方式，较小类型并不是较大类型的子类型。因此较小的类型不能隐式转换为较大的类型。
 * 这意味着在不进行显式转换的情况下我们不能把 Byte 型值赋给一个 Int 变量。只能使用显式转换来拓宽数字
 */
fun numberTransform() {
    val b: Byte = 1 // OK, 字面值是静态检测的
    //val i: Int = b // 错误
    val i: Int = b.toInt() // OK: 显式拓宽
    /*
    toByte(): Byte
    toShort(): Short
    toInt(): Int
    toLong(): Long
    toFloat(): Float
    toDouble(): Double
    toChar(): Char
     */
    //而算术运算会有重载做适当转换
    val l = 1L + 3 // Long + Int => Long
}


/** 数字运算:Kotlin支持数字运算的标准集 */
fun numberOperation() {
    /*
    shl(bits) – 有符号左移 (Java 的 <<)
    shr(bits) – 有符号右移 (Java 的 >>)
    ushr(bits) – 无符号右移 (Java 的 >>>)
    and(bits) – 位与
    or(bits) – 位或
    xor(bits) – 位异或
    inv() – 位非
     */
    val a = 12345
    val b = 21
    println(a shl 3)
    println(a or 3)
    println(a.inv())
}