package me.ztiany.operators


private class Point1(val x: Int, val y: Int) {
    override fun equals(obj: Any?): Boolean {
        if (obj === this) return true
        if (obj !is Point1) return false
        return obj.x == x && obj.y == y
    }
}

fun main(args: Array<String>) {
    println(Point1(10, 20) == Point1(10, 20))
    println(Point1(10, 20) != Point1(5, 5))
    println(null == Point1(1, 2))
}
