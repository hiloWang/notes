package me.ztiany.operators


private data class Point3(val x: Int, val y: Int)

private data class Rectangle(val upperLeft: Point3, val lowerRight: Point3)

private operator fun Rectangle.contains(p: Point3): Boolean {
    return p.x in upperLeft.x until lowerRight.x &&
            p.y in upperLeft.y until lowerRight.y
}

fun main(args: Array<String>) {
    val rect = Rectangle(Point3(10, 20), Point3(50, 50))
    println(Point3(20, 30) in rect)
    println(Point3(5, 5) in rect)
}
