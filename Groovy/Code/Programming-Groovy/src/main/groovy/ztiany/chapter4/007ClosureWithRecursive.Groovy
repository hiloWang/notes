package ztiany.chapter4;

//--------------------
//使用尾递归编写程序
//--------------------

/*
使用递归会遇到一些问题(StackOverFlowError)，而是用Groovy的闭包可以获得递归的优势同时避免递归的问题
:例如：
 */

def factorial(BigDecimal number) {
    if (number == 1) {
        1
    }else {
        number * factorial(number -1)
    }
}

try {
    println "factorial of 5 is ${factorial(5)}"//计算5的阶乘
    println "number of bits in the result is ${factorial(5000).bitCount()}"//计算5000的阶乘
    /*
    结果是：
    factorial of 5 is120
    caught java.lang.StackOverflowError
     */
} catch (Throwable throwable) {
    println "caught ${throwable.class.name}"
}
//把递归转换成迭代过程，Groovy闭包使用一个trampoline(蹦床)方法实现
Closure factorial
factorial = {
    int number , BigInteger theFactorial->
        number == 1?
                  theFactorial://返回1
                  factorial.trampoline(number-1,number*theFactorial)//返回一个trampolineClosure实例
}.trampoline()

println "factorial of 5 is ${factorial(5,1)}"//计算5的阶乘
println "number of bits in the result is ${factorial(5000,1).bitCount()}"//计算5000的阶乘

/*
这里定义了一个变量factorial，并把一个表赋值给它，改必要接受两个参数，一个是number，要计算的阶乘，一个是theFactorial，表示通过
递归计算出的部分结果。


factorial变量本身被赋的就是在闭包上次掉哦那个trampoline方法的结果

Groovy上的这种尾递归实现：当我们调用trampoline方法是，该闭包会返回一个特殊类TrampolineClosure的一个实例，
当我们向该闭包传递参数时，其实就是调用该对象的call方法，该方法使用了一个简单的for循环来调用闭包上的call方法
，直到不产生trampolineClosure实例，这种技术在背后将递归转换成了一个简单的迭代


//带有参数的trampoline方法会curr化所传入的参数
    public Closure<V> trampoline(final Object... args) {
        return new TrampolineClosure<V>(this.curry(args));
    }




trampolineClosure的call方法实现

  private V loop(final Object lastResult) {//传入1或者一个trampolineClosure实例
        Object result = lastResult;

        for (;;) {
            if (result instanceof TrampolineClosure) {//是trampolineClosure，继续调用original的call方法
                result = ((TrampolineClosure)result).original.call();/这里的参数已经被curry化了
            } else return (V) result;
        }
    }

 */

/*
使用Trampoline让我们避免了闭包的缺陷，但是这样一来简洁性也失去了，这里可以给BigInteger提供一个默认的值，
BigInteger = 1,调用者可以省略第二个参数，但是还需要在封装一层，防止他们传多个参数*/

def factorialEnhance(int factorialFor) {
    def tailFactorial
    tailFactorial = {
        int number , BigInteger theFactorial = 1->
            number == 1?
                    theFactorial://返回1
                    tailFactorial.trampoline(number-1,number*theFactorial)//返回一个trampolineClosure实例
    }.trampoline()

    tailFactorial.call(factorialFor)
}

println "factorialEnhance 50 = " +factorialEnhance(50)
//需要注意的是使用trampoline时，性能会有所折扣


