package ztiany.chapter4

//--------------------
//闭包的使用方式
//--------------------
/*
前面介绍了如何定义方法调用参数的参数时即时创建闭包，此外还可以将闭包赋值给变量
 */

def totalSelectValue(n, closure) {
    total = 0
    for (i in 1..n) {
        if (closure.call(i)) {
            total += i
        }
    }
    total
}

def isOdd = {//闭包也不需要指明返回值，类似于方法
    it % 2 != 0
}
println totalSelectValue(20, isOdd)

class Equipment {
    def calculator
    Equipment ( calc ){
        calculator = calc
    }

    def simulate() {
        println 'simulate run'
        calculator.call()
    }
}



def eq1 = new Equipment({
    println 'eq1 run'
})
def aCalc = {
    println( 'aCalc run')
}
eq2 = new Equipment(aCalc)
eq3  = new Equipment(aCalc)
eq1.simulate()
eq2.simulate()
eq3.simulate()

