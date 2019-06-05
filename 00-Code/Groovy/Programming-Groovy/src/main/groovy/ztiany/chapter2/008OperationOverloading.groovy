package ztiany.chapter2

//--------------------
//操作符重载
//-------------------
/*

    Groovy支持操作符重载，可以巧妙的应用这一点来创建DSL领域特定语言，java不支持，Groovy的做法的每一个操作符都会映射到一个标准的方法，在java可以使用的那些方法
    在groovy中既可以使用操作符，也可以使用与之对应的方法

 */

//例如 ,这里的ch++ 映射到的是String的next方法
for (ch = 'a'; ch < 'd'; ch++) {
    println(ch)
}
for (ch in 'a'..'d') {
    println(ch)
}
//String和ArrayList重载了很多的操作符，如ArrayList的leftShift
list = []
list << "d"
println list

//下面为一个类添加一个+的操作符
class ComplexNumber {
    def real, imaginary

    def plus(other) {
        new ComplexNumber(real: real + other.real, imaginary: imaginary + other.imaginary)
    }

    @Override
    public String toString() {
        return "ComplexNumber{" +
                "real=" + real +
                ", imaginary=" + imaginary +
                '}';
    }
}

println new ComplexNumber(real: 2, imaginary: 4) + new ComplexNumber(real: 5, imaginary: 3)
//当应用于某个上下文时，操作符重载可以使代码更富于表现力，应该只重载那些能够使事物变得显而易见的操作符

/*
下面的表格描述了groovy中的操作符所映射到的方法：


Operator      |      Method
------      |       ------
a + b          |     a.plus(b)
a – b        |   a.minus(b)
a * b       |    a.multiply(b)
a ** b     |     a.power(b)
a / b       |    a.div(b)
a % b     |      a.mod(b)
a | b      |     a.or(b)
a & b      |     a.and(b)
a ^ b      |     a.xor(b)
a++ or ++a       |   a.next()
a– or –a         |   a.previous()
a[b]         |   a.getAt(b)
a[b] = c    |        a.putAt(b, c)
a << b        |  a.leftShift(b)
a >> b        |  a.rightShift(b)
switch(a) { case(b) : }     |    b.isCase(a)
~a      |    a.bitwiseNegate()
-a      |    a.negative()
+a       |   a.positive()


 */

