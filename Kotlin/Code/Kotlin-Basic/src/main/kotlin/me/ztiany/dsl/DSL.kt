package me.ztiany.dsl

/** DSL
 *
 * 领域特定语言，用于针对某一个领域而构建的语言，比如 SQL 和 正则表达式。DSL 可以分为`外部 DSL` 和 `内部 DSL`。
 *
 * Kotlin  DSL 构建，Kotlin 的 DSL 是完全静态的
 *
 *      DSL 构建用到的关键特性：
 *
 *          -  带接收者的函数字面
 *          -  中缀表达
 *          -  函数扩展
 *          -  invoke 约定
 *          -  gradle + kotlin
 *
 * */


open class Tag(val name: String) {

    private val children = mutableListOf<Tag>()

    protected fun <T : Tag> doInit(child: T, init: T.() -> Unit) {
        child.init()
        children.add(child)
    }

    override fun toString() = "<$name>${children.joinToString("")}</$name>"
}

fun table(init: TABLE.() -> Unit) = TABLE().apply(init)

class TABLE : Tag("table") {
    fun tr(init: TR.() -> Unit) = doInit(TR(), init)
}

class TR : Tag("tr") {
    fun td(init: TD.() -> Unit) = doInit(TD(), init)
}

class TD : Tag("td")

fun createTable() =
        table {
            tr {
                td {
                }
            }
        }

fun createAnotherTable() = table {
    for (i in 1..2) {
        tr {
            td {
            }
        }
    }
}

fun main(args: Array<String>) {
    println(createTable())
    println(createAnotherTable())
}
