package me.ztiany.functions

/** 函数引用   */
fun main(args: Array<String>) {

    //包级函数引用
    args.forEach(::println)

    //引用了类上的函数，此时helloWorld含有一个隐式的参数，即Hello对象
    val helloWorld = Hello::world

    args.filter(String::isNotEmpty)

    //对象上的函数引用，
    val pdfPrinter = PdfPrinter()
    args.forEach(pdfPrinter::println)
}

class PdfPrinter {
    fun println(any: Any) = kotlin.io.println(any)
}

class Hello {
    fun world() = println("Hello World.")
}

//返回函数
fun makeFun(): () -> Unit {
    var count = 0
    return { println(count++) }
    //return fun () = println(count++)
}