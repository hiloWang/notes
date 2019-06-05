package chapter06

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

fun main(args: Array<String>) {
    html {
        "id"("HtmlId")
        "head"{
            "id"("headId")
        }
        body {
            id="bodyId"
            `class` = "bodyClass"

            "a"{
                "href"("https://www.kotliner.cn")
                +"Kotlin 中文博客"
            }
        }

        "div"{

        }
    }.render().let(::println)
}

fun html(block: Tag.()->Unit): Tag{
    return Tag("html").apply(block)
}

fun Tag.head(block: Head.()->Unit){
    this + Head().apply(block)
}

fun Tag.body(block: Body.()->Unit){
    this + Body().apply(block)
}

class StringNode(val content: String): Node{
    override fun render() = content
}

class Head: Tag("head")

class Body: Tag("body"){
    var id by MapDelegate(proerties)

    var `class` by MapDelegate(proerties)
}



open class Tag(val name: String): Node{

    val children = ArrayList<Node>()

    val proerties = HashMap<String, String>()

    operator fun String.invoke(value: String){
        proerties[this] = value
    }

    operator fun String.invoke(block: Tag.()->Unit){
        this@Tag.children.add(Tag(this).apply(block))
    }

    operator fun String.unaryPlus(){
        children.add(StringNode(this))
    }

    operator fun plus(node: Node){
        children.add(node)
    }

    // <html id="htmlId" style=""><head> </head> <body></body></html>
    override fun render(): String {
        return StringBuilder()
                .append("<")
                .append(name)
                .let {
                    stringBuilder ->
                    if(!this.proerties.isEmpty()){
                        stringBuilder.append(" ")
                        this.proerties.forEach{
                            stringBuilder.append(it.key)
                                    .append("=\"")
                                    .append(it.value)
                                    .append("\" ")
                        }
                    }
                    stringBuilder
                }
                .append(">")
                .let {
                    stringBuilder ->
                    children.map(Node::render).map(stringBuilder::append)
                    stringBuilder
                }
                .append("</$name>")
                .toString()
    }

}

interface Node {
    fun render(): String
}

class MapDelegate(val map: MutableMap<String, String>): ReadWriteProperty<Any, String> {
    override fun getValue(thisRef: Any, property: KProperty<*>): String {
        return map[property.name]?: ""
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: String) {
        map[property.name] = value
    }
}