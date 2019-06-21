package chapter05

/**闭包*/

fun makeFun(): () -> Unit {
    var count = 0
    return fun() {
        println(++count)
    }
}

fun fibonacci(): Iterable<Long> {
    var first = 0L
    var second = 1L
    return Iterable {
        object : LongIterator() {
            override fun nextLong(): Long {
                val result = second
                second += first
                first = second - first
                return result
            }

            override fun hasNext() = true

        }

    }
}

fun main(args: Array<String>) {
    val add5 = add(5)
    println(add5(2))

    for (i in fibonacci()) {
        if (i > 100) {
            break
        }
        println(i)
    }
}

//fun add(x: Int) = fun(y: Int) = x + y 等价于下面函数
fun add(x: Int): (Int) -> Int {
     return fun(y: Int): Int {
        return x + y
    }
}
