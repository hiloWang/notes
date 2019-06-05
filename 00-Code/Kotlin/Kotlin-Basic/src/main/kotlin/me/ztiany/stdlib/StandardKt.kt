package me.ztiany.stdlib

/** kotlin/util/Standard.kt API学习 */
fun main(args: Array<String>) {
    letSample()
    applySample()
    runSample()
    alsoSample()
    withSample()
}

fun withSample() {
    println("withSample--------------")
    val result = with(100) {
        val a = toString()
        a
    }
    println(result)
}

fun alsoSample() {
    println("alsoSample--------------")
    val abc = "ABC".also {
        println(it)
    }
    println(abc)
}

fun runSample() {
    println("runSample--------------")
    println(100.run {
        println(this)
        "100 return"
    })
}

fun applySample() {
    println("applySample--------------")
    val result = 4.apply {
        println(this)
    }
    println(result)

    val list = mutableListOf<String>()

    list.apply {
        add("A")
        add("B")
    }
}

private fun letSample() {
    println("letSample--------------")
    val abc = 1.let {
        println(it)
        "ABC"
    }
    println(abc)
}