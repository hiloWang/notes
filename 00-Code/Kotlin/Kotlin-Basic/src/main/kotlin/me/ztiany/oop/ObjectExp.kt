package me.ztiany.oop

import java.awt.Frame
import java.awt.Window
import java.awt.event.ActionListener
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import java.io.Closeable


/**
 * 对象表达式：要创建一个继承自某个（或某些）类型的匿名类的对象
 *
 * 1，如果超类型有一个构造函数，则必须传递适当的构造函数参数给它。 多个超类型可以由跟在冒号后面的逗号分隔的列表指定
 * 2，匿名对象可以用作只在本地和私有作用域中声明的类型。如果你使用匿名对象作为公有函数的 返回类型或者用作公有属性的类型
 *      那么该函数或属性的实际类型 会是匿名对象声明的超类型，如果你没有声明任何超类型，就会是 Any。
 * 3，在匿名对象中添加的成员将无法访问。
 */

private fun objectExpression() {

    val window: Window = Window(Frame())

    window.addMouseListener(object : MouseAdapter() {
        override fun mouseClicked(e: MouseEvent) {
        }

        override fun mouseEntered(e: MouseEvent) {
        }
    })

    //如果对象是函数式 Java 接口（即具有单个抽象方法的 Java 接口）的实例， 你可以使用带接口类型前缀的lambda表达式创建它：
    val listener = ActionListener { println("clicked") }

    val com = object : Closeable {
        override fun close() {
        }
    }

    //任何时候，如果我们只需要“一个对象而已”，并不需要特殊超类型
    val adHoc = object {
        var x: Int = 0
        var y: Int = 0
    }
    print(adHoc.x + adHoc.y)
}
