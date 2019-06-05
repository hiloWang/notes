package ztiany.chapter4

//--------------------
//闭包与协程
//--------------------
/*

调用一个函数或方法会在程序的执行序列中创建一个新的作用域，我们会在一个入口点进入函数，在方法完成之后，回到调用者的作用域

协程(Coroutine)则支持多个入口点，每个入口点都是上次挂起调用的位置，我们可以进入一个函数，执行部分代码后挂起，在回到调用的位置
的上下文或作用域内执行一个代码，之后我们可以在挂起的地方恢复该函数的执行，
 */

def iterate(n, closure) {
    1.upto(n) {
        println "in iterate with value $it"
        closure.call(it)
    }
}

total = 0
iterate(10){
    total += it
    println "in closure total so fir is $total"
}
//每次调用闭包，我们都会从上次调用中恢复total的值

