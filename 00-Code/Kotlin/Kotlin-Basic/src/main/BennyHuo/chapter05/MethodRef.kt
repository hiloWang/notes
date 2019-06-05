package chapter05

/**方法引用*/
fun main(args: Array<String>) {

    args.forEach(::println)

    val helloWorld = Hello::world

    /*引用类上的方法，isNotEmpty其实是需要一个参数的，那就是 CharSequence 自身*/
    args.filter(String::isNotEmpty)

    /*引用对象上的方法*/
    val pdfPrinter = PdfPrinter()
    args.forEach(pdfPrinter::println)

}

class PdfPrinter {
    fun println(any: Any) {
        kotlin.io.println(any)
    }
}

class Hello {
    fun world() {
        println("Hello World.")
    }
}