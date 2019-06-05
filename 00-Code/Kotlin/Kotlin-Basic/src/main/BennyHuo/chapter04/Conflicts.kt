package chapter04

/**继承与实现的冲突*/
abstract class A {
    open fun x(): Int = 5
}

interface B {
    fun x(): Int = 1
}

interface C {
    fun x(): Int = 0
}

class D(var y: Int = 0) : A(), B, C {

    override fun x(): Int {
        println("call x(): Int in D")
        return when {
            y > 0 -> y
            y < -200 -> super<C>.x()
            y < -100 -> super<B>.x()
            else -> super<A>.x()
        }
    }
}

fun main(args: Array<String>) {
    println(D(3).x())
    println(D(-10).x())
    println(D(-110).x())
    println(D(-10000).x())
}