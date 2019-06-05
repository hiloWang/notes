package ztiany.chapter4

//--------------------
//向闭包传递参数
//--------------------
/*
此节来探讨，如何行闭包传递多个参数

当闭包只接受一个参数时，可以使用it引用参数，如果传递的参数多余一个，就需要通过名字一一列出，如下面例子：
 */

def tellFortune(closure) {
    closure(new Date("09/20/2012"), 'your day is filled with ceremony')
}

tellFortune() {
    data, str ->
        println "fortune for ${data} is '${str}'"
}
/*
符号->将闭包的参数声明和闭包的主体分隔，同时闭包中也可以指定参数的类型，
如果为参数选择表现力好大名字，通常可以避免定义类型，后面会看到，在元编程中可以使用闭包来覆盖或者替代方法，而在那种情况下
类型信息对确定实现的正确行非常重要
 */

//--------------------
//使用闭包进行资源清理
//--------------------
/*
对于某些资源密集型对象，通常可以看到有close或者destroy方法用于释放资源，但是使用这些类的人可能忘记调用这些方法，闭包可以确保
这些方法被调用如：


write = new FileWriter("out put.txt")
write.write("/")
//忘记调用write.close()
使用Groovy的withWriter方法重写这段代码，当从闭包返回时，withWriter会自刷新并关闭整个流
 */
new FileWriter("out.txt").withWriter {
    it.write('我叫Ztiany， 我正在学Groovy')
}
/*现在不必关心流的关闭了，我们可以集中精力完成工作，也可以在自己的类中实现这样的便捷方;
比如要给Resource类，希望在调用它的任何其他方法之前，先调用open方法，使用完成时还需要调用close方法释放资源
*/

class Resource {

    def isOpen = false

    def open() {
        isOpen = true
        println "open resource"
    }

    def read() {
        check()
        println 'read source'
    }

    def write() {
        check()
        println 'write source'
    }

    def close() {
        check()
        println "close resource"
    }

    private void check() {
        if (!isOpen) {
            throw new RuntimeException("you should call open before you call other methods")
        }
    }
}
//下面是常规的使用方式
rs1 = new Resource()
rs1.open()
rs1.write()
rs1.read()
rs1.close()
//然后闭包可以帮忙，可以使用，Execute Around Method模式来处理这个问题


class Resource1 {

    def isOpen = false

    def open() {
        isOpen = true
        println "open resource1"
    }

    def read() {
        check()
        println 'read source1'
    }

    def write() {
        check()
        println 'write source1'
    }

    def close() {
        check()
        println "close resource1"
    }

    static def use(closure) {
        def r = new Resource1()
        try {
            r.open()
            closure(r)
        }finally {
            r.close()
        }
    }

    private void check() {
        if (!isOpen) {
            throw new RuntimeException("you should call open before you call other methods")
        }
    }
}

Resource1.use(){
    it.read()
    it.write()
}//多亏了闭包，现在close的调用时自动的，确定性的！！！



/*
，Execute Around Method:如果有一对必须连续执行的动作，如果打开关闭，我们就可以使用，Execute Around Method模式
编写一个Execute Around Method，它接受一个代码块，把该代码块的调用夹到多对方法的调用之间。使用者不必担心这对动作，它们会自动调用
 */

