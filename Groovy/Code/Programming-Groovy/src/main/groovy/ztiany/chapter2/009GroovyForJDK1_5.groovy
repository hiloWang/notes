package ztiany.chapter2

import static Math.random as rand

//--------------------
//Groovy对java1.5的支持
//--------------------
/*
下面是JDK1.5的新特性
- 自动装箱
- for-each循环
- enum
- 变长参数
- 注解
- 静态导入
- 泛型
 */

// 自动装箱，Groovy从一开始就支持自动装箱，必要时Groovy会将基本类型视作对象
int val = 4;
println val.class.getName()//java.lang.Integer
//虽然定义的是int，但是创建的Integer实例，而不是基本类型，Groovy会根据该实例的使用方式来决定将其存储为int类型还是Integer类型
//Groovy2.0之前，所有的基本类型都会视作对象，之后为了改善性能，只有在需要的时候才会被视作对象


//fro each groovy对循环的支持强于java，传统的增强for循环，groovy需要指明类型，而用in 则不需要
list = [1, 2, 3, 4]
for (int i : list) {
    println i
}
for (i in list) {
    println i
}



//枚举 Groovy同样支持枚举，它是类型安全的

enum CoffeeSize{
    Short, Small, Medium, Large,Mug
}

def orderCoffee(size) {
    switch (size) {
        case [CoffeeSize.Short, CoffeeSize.Small]:
            println('111111111')
            break
        case [CoffeeSize.Medium,CoffeeSize.Large]:
            println('22222222')

            break
        case CoffeeSize.Mug:
            println('333333')
            break
    }
}

orderCoffee CoffeeSize.Short
orderCoffee CoffeeSize.Small
orderCoffee CoffeeSize.Medium
orderCoffee CoffeeSize.Large
orderCoffee CoffeeSize.Mug

for (coffee in CoffeeSize.values()) {
    println coffee.name()
}


//变长参数 Groovy以两种形式支持Java的编程参数，一种是用... 另一种是以数据为末尾的形参方法

def receiveVarArgs(int a, int ... b) {
    println "You passed $a and $b"
}

def receiveArray(int a, int [] b) {
    println "You passed $a and $b"
}
receiveVarArgs 1,3,5,6,8
receiveArray 1,2,32,34,54
//在向带数组形参的方法传递数组时，需要注意.[1,2,3]默认类型为List，所以传递数据是需要使用下面方法

int[] arr = [1, 3, 7, 77]
arr2 = [3, 44, 34, 65] as int[]

receiveArray 1, arr
receiveArray 4, arr2




// 注解
/*
Java使用注解来表示元数据，Java5中也定义了一些注解，@Override，@Deprecated，@SuppressWarnings,
Groovy也可以定义和使用注解，使用方式与java相同，但对于Java编译相关的注解，Groovy的理解会有所不同，比如Groovy会忽略Override注解
 */


//静态导入，Groovy不仅支持静态导入，还支持静态导入别名

double value = rand()
println value



//泛型
/*
Groovy      是支持可选类型的动态类型语言，作为java的超级它也支持泛型，但Groovy编译器不会像Java编译器那样检查类型，
Groovy的动态类型和泛型类型互相作用使我们的代码运行起来
 */

ArrayList<Integer> list = new ArrayList<>()
list.add(33)
list.add("Dd")
for (int i   : list) {//Cannot cast object 'Dd' with class 'java.lang.String' to class 'int'
    println i
}
//在add方法中，groovy只会把类型看做一种建议，对集合进行循环的时候，Groovy会尝试将元素转换成int类型，这是就导致错误



















