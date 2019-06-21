package chapter05

/**高阶函数*/
fun main(args: Array<String>) {
    //floatMap 铺平包含集合的集合
    listOf(1..10, 20..40, 60..80)
            .flatMap {
                it.toList()
            }
            .joinTo(buffer = StringBuffer())
            .let {
                println(it)
            }

    //reduce，组合所有元素
    (1..10).toList()
            .reduce { acc, i ->
                println("acc = $acc , i = $i")
                acc + i
            }
            .let {
                println(it)
            }

    //fold 折叠所有元素，fold 相较于 reduce 可以添加一个初始值
    listOf(1, 2, 3)
            .fold(6) { i: Int, i1: Int ->
                println(" i = $i i1 = $i1")
                i + i1
            }
            .apply {
                println(this)
            }

    //takeWhile，遇到第一个不满足条件的元素结束迭代
    (1..10).toList()
            .takeWhile {
                it <= 5
            }
            .joinToString()
            .let {
                println(it)
            }

}

