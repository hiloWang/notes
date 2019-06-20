package me.ztiany.operators


data class Point2(val x: Int, val y: Int)

operator fun Point2.get(index: Int): Int {
    return when (index) {
        0 -> x
        1 -> y
        else ->
            throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}

data class MutablePoint(var x: Int, var y: Int)

operator fun MutablePoint.set(index: Int, value: Int) {
    when(index) {
        0 -> x = value
        1 -> y = value
        else ->
            throw IndexOutOfBoundsException("Invalid coordinate $index")
    }
}


fun main(args: Array<String>) {
    println("-------------------------------------------")
    val p = Point2(10, 20)
    println(p[1])
    println("-------------------------------------------")
    val mp = MutablePoint(10, 20)
    mp[1] = 42
    println(p)
}
