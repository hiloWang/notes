package ztiany.chapter4;

//--------------------
//科里化闭包
//--------------------
/*
带有预绑定形参的闭包叫做科里化闭包，当对一个闭包调用curry时，就是要求预先绑定某些形参，在预先绑定一个形参后，
调用这个闭包就没有必要为这个形参提供实参了。
 */

def tellFortunes(closure) {
    Date date = new Date()
    postFotrune = closure.curry(date)//预先绑定形参date
    postFotrune "you day is filled wih ceremony"
    postFotrune "they`re features , not bugs"
}

tellFortunes() {
    date, fortune ->
        println "fortune for $date is ${fortune}"//直接使用date
}

/*
可以使用curry方法科里化任意多个形参，但这些形参必须是从前面开始的若干个参数。也就是说，如果有n个形参，我们可以科里化前k个，其中0<=k<=n

如果要科里化形参中间的值，可以使用ncurry方法，传入要进行科里化的形参的位置和对应的值
 */

//--------------------
//动态闭包
//--------------------
/*
可以确定一个闭包是否已经提供了，如果尚未提供，我们可以决定使用一个默认的实现来代替：
 */

def doSomthing(closure) {
    if (closure) {
        closure()
    } else {
        println "Using default implementation"
    }
}

doSomthing() {
    println "using specialized implementation"
}
doSomthing()
/*
在传递参数是也很有灵活性，可以动态的确定一个表达所期望的参数的数目与类型：
 */

def completeOrder(amount, taxComputer) {

    tax = 0;
    if (taxComputer.maximumNumberOfParameters == 2) {
        //期望传入汇率 , maximumNumberOfParameters表示闭包接受的参数的个数
        tax = taxComputer.call(amount, 6.05)
    } else {
        tax = taxComputer.call(amount)
    }
}

println completeOrder(100) {
    it * 0.0825
}
println completeOrder(100) {
    amount, rate ->
        amount * (rate / 100)
}
//还可以使用parameterTypes属性或者getParameterTypes方法获知这些参数的类型：

def examine(closure) {
    println "$closure.maximumNumberOfParameters parameter(s) given:"
    for (aParameter in closure.parameterTypes) {
        println aParameter.name
    }
    println "--"
}

examine(){}
examine() {
    it
}
examine() {
    val ->
}
examine() {
    Date val1 ->
}
examine() {
    Date val1, val2 ->
}

examine() {
    Date val1, String val2 ->
}

/*
即使一个闭包没有声明任何形参，就是{},{it},其实他也会接受一个参数，如果调用者没有向闭包提供任何值，则第一个形参it为null，如果希望闭包不接受任何参数，可以使用
{->}语法，在->之前没有任何形参
 */



