package me.ztiany.advance



/**
 * Kotlin中集合：
 *
 *      1，Kotlin 区分可变集合和不可变集合（lists、sets、maps 等）。精确控制什么时候集合可编辑有助于消除 bug 和设计良好的 API。
 *      2，Kotlin 没有专门的语法结构创建 list 或 set。 要用标准库的方法，如 listOf()、 mutableListOf()、 setOf()、 mutableSetOf()
 *      3，在非性能关键代码中创建 map 可以用一个简单的惯用法来完成：mapOf(a to b, c to d)
 */

private open class Shape

private class Square : Shape()

private fun useCollections() {
    //可变集合
    val mutableList = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
    mutableList.clear()

    //listOf创建的是只读集合
    val readOnlyList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9)

    //创建set
    val strSet = hashSetOf("a", "b", "c", "c")

    //在非性能关键代码中创建 map 可以用一个简单的惯用法来完成
    val map = mapOf("A" to 1, "B" to 2)

    //协变性，不可变集合具有协变性
    var listShape: List<Shape> = listOf()
    val listSquare: List<Square> = listOf()
    listShape = listSquare

    //对于可变集合，使用toList()可以返回一个快照
    val intList = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
    val readOnlyIntList = intList.toList()
}


/*

Kotlin为操作集合容器添加了许多的函数式API，操作即可变得更加方便简洁。

下标操作类

    contains —— 判断是否有指定元素
    elementAt —— 返回对应的元素，越界会抛IndexOutOfBoundsException
    firstOrNull —— 返回符合条件的第一个元素，没有 返回null
    lastOrNull —— 返回符合条件的最后一个元素，没有 返回null
    indexOf —— 返回指定元素的下标，没有 返回-1
    singleOrNull —— 返回符合条件的单个元素，如有没有符合或超过一个，返回null

判断类

    any —— 判断集合中 是否有满足条件 的元素
    all —— 判断集合中的元素 是否都满足条件
    none —— 判断集合中是否 都不满足条件，是则返回true
    count —— 查询集合中 满足条件 的 元素个数
    reduce —— 从 第一项到最后一项进行累计

过滤类

    filter —— 过滤 掉所有 满足条件 的元素
    filterNot —— 过滤所有不满足条件的元素
    filterNotNull —— 过滤NULL
    take —— 返回前 n 个元素

转换类

    map —— 转换成另一个集合（与上面我们实现的 convert 方法作用一样）;
    mapIndexed —— 除了转换成另一个集合，还可以拿到Index(下标);
    mapNotNull —— 执行转换前过滤掉 为 NULL 的元素
    flatMap —— 自定义逻辑合并两个集合；
    groupBy —— 按照某个条件分组，返回Map；

排序类

    reversed —— 反序
    sorted —— 升序
    sortedBy —— 自定义排序
    sortedDescending —— 降序
 */
private fun functionalApi() {

}

fun main(args: Array<String>) {
    useCollections()
}
