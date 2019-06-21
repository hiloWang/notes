package ztiany.chapter2

//--------------------
//3 gdk一瞥
//--------------------

//通过Process获取信息

println "git help".execute().text;//String 的execute方法用于执行String表达的命令
println "git help".execute().getClass().name;//java.lang.ProcessImpl

println "cmd /C dir".execute().text//dir并不是一个程序，而只是一个shell命令，所以需要通过调用cmd来执行dir命令


//--------------------
//4 安全导航操作符
//--------------------

//java中需要进行各种判断来避免空指针，而groovy使用操作符"?."
def revereStringSafe(String str) {
    str?.reverse()
}

println revereStringSafe("ddffaa")
println revereStringSafe(null)



//--------------------
//5 异常处理
//--------------------

//Groovy并不强制处理任何异常，我们不处理的异常都会别自动的传递到上一层

def openFile(fileName) {
    new FileInputStream(fileName)//这里并没有强制我们检测一次
}

try {
    openFile(openFile("noExist"))//这里会抛出异常
} catch (Exception ex) {
    println ex
}

//如果需要捕获的是Exception也可使用如下方式：

try {
    openFile(openFile("noExist"))//这里会抛出异常
} catch (ex) {//可以省略掉Exception，但是对于Error，Throwable就不能省略了
    println ex
}


//--------------------
//6  静态方法中可以使用this
//--------------------
class Wizard {
    def static learn(int a) {
        println(a)
        this
    }
}

Wizard.learn(1).learn(3).learn(4)











