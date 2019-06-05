package me.ztiany.oop

import java.awt.Frame
import java.awt.Window
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent


/**
 * 内部类：
 *      1， 类可以嵌套在其他类中，默认为静态内部类
 *      2，类可以标记为 inner 以便能够访问外部类的成员。内部类会带有一个对外部类的对象的引用
 */

private class Outer {

    private val bar: Int = 1

    inner class Nested1 {
        fun foo() = 2
    }

    class Nested2 {
        fun foo() = 2
    }
}

//访问方式不一样的
private val nested1 = Outer().Nested1().foo() // == 2
private val nested2 = Outer.Nested2().foo() // == 2


/**
 * 匿名内部类：使用对象表达式创建匿名内部类实例，如果对象是函数式Java接口（即具有单个抽象方法的 Java 接口）的实例，
 *                     可以使用带接口类型前缀的lambda表达式创建它
 */
private fun testAnonymity() {

    val window: Window = Window(Frame())

    window.addMouseListener(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
            // ……
        }

        override fun mouseEntered(e: MouseEvent) {
            // ……
        }
    })

    //如果对象是函数式Java接口（即具有单个抽象方法的 Java 接口）的实例， 可以使用带接口类型前缀的lambda表达式创建它
    val listener = ActionListener { println("clicked") }
}
