package me.ztiany.basic


/** 字符串：字符串用 String 类型表示。字符串是不可变的。 字符串的元素——字符可以使用索引运算符访问: s[i]。*/
private fun accessStr() {
    val str = "Hi, I am a developer"
    print(str[1])
    print(str[2].javaClass)
    println()
    //  可以用 for 循环迭代字符串:
    for (char in str) {
        print(char)
        print(',')
    }
}

/**  Kotlin 有两种类型的字符串字面值: 转义字符串可以有转义字符，以及原生字符串可以包含换行和任意文本。*/
private fun strLiteral() {
    //转义字符串很像 Java 字符串:
    val s = "Hello, world!\n"

    //原生字符串 使用三个引号（"""）分界符括起来，内部没有转义并且可以包含换行和任何其他字符:
    val text =
            """
            for (c in "foo")
                print(c)
        """

    //trimMargin    函数去除前导空格，默认 | 用作边界前缀
    val table = """
    |Tell me and I forget.
    |Teach me and I remember.
    |Involve me and I learn.
    |(Benjamin Franklin)
    """
    println()
    println("table---------------------------------")
    println(table.trimMargin())

    val table1 = """
    >Tell me and I forget.
    >Teach me and I remember.
    >Involve me and I learn.
    >(Benjamin Franklin)
    """
    println("table1---------------------------------")
    println(table1.trimMargin(">"))
}

/**
 * 字符串模板:
 *   1，字符串可以包含模板表达式 ，即一些小段代码，会求值并把结果合并到字符串中。 模板表达式以美元符（$）开头，由一个简单的名字构成
 *  2，原生字符串和转义字符串内部都支持模板。
 *  3，$ 字符（它不支持反斜杠转义）
 */
private fun strTemplate() {
    val i = 10
    val s1 = "i = $i" // 求值结果为 "i = 10"

    //花括号扩起来的任意表达式
    val s2 = "abc"
    val str = "$s2.length is ${s2.length}" // 求值结果为 "abc.length is 3"

    // 如果你需要在原生字符串中表示字面值 $ 字符
    val price = """
                ${'$'}9.99
                """
}

private fun splitSample() {
    val list1 = "12.345-6.A".split("\\.".toRegex())
    println(list1)
    val list2 = "12.345-6.A".split(".","-")
    println(list2)
}


fun main(args: Array<String>) {
    accessStr()
    strLiteral()
    splitSample()
}