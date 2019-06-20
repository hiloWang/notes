package ztiany.chapter2

import groovy.transform.Canonical
import groovy.transform.InheritConstructors

//--------------------
//使用Groovy生成代码
//--------------------

//Groovy在groovy.transform包和其他一些包中提供了很多代码生成的注解

//Canonical 用于生成toString
@Canonical(excludes = 'lastName')
class Person {
    def firstName
    def lastName
    def age
}

println new Person(firstName: "Zhan", lastName: "Tianyou", age: 26)

//Delegate 只有当派生的类是真的可替换的，而且可代替基类，继承才显示出其优势，从存粹的代码复用角度来讲，委托要由于继承，而Java
//中使用委托会是代码变得冗余，而且需要更多工作，Groovy是委托变得容易

class Worker {
    def work() {
        println "get work done"
    }

    def analyze() {
        println "analyze......"
    }

    def writeReport() {
        println 'get report written...'
    }
}

class Expert {

    def analyze() {
        println "Expert analyze......"
    }
}

class Manager {
    @Delegate
    Expert mExpert = new Expert()
    @Delegate
    Worker mWorker = new Worker()
}

bernie = new Manager()
//在编译时，Groovy会检查Manager类，如果该类中没有被委托的方法，就把这些方法从北委托的类中引进来，因此，首先
//它会引入Expert类的analyze方法，而从Worker类中只会把work和writeReport方法引进来，因为analyze方法已经从Expert引入
bernie.analyze()
bernie.work()
bernie.writeReport()

//Lazy
/*
我们想把耗时对象的创建推迟到真正需要时，完全可以懒惰与高效并得，编写更好的代码，同时又能获得懒惰初始化的所有好处
 */

class Heavy {
    def size = 10;

    Heavy() {
        println "Creating heavy with $size"
    }
}

class AsNeeded {

    def value

    @Lazy
    Heavy mHeavy1 = new Heavy()
    @Lazy
    Heavy mHeavy2 = {
        new Heavy(size: value)
    }()//这里是调用闭包的方法

    AsNeeded() {
        println "Creating AsNeeded"
    }
}

def asNeed = new AsNeeded(value: 10000)

println asNeed.mHeavy1.size
println asNeed.mHeavy1.size
println asNeed.mHeavy2.size
//Lazy不仅推迟了对象的创建，将将字段标志位volatile的
//Lazy的第二个好处是提供给了一种轻松实现线程安全的虚拟代理模式

//使用@Newify
/*
Groovy经常按照传统的Java语法创建对象:new,    然而在创建DSL时，去掉这个关键字表达会更流畅，@Newify可以帮助创建类似Ruby的构造器，于是new变成了
该类的一个方法。
这个注解还可以用来创建类似Python的构造器，可以完全去掉new，此时@Newify必须指定类型，只有将auto=false这个元素据值设置给@Newify，才能创建类似
Ruby的构造器
 */

class CreditCard {
    def number
    def areaCode

}

@Newify([Person, CreditCard])

def fluentCreate() {
    println Person.new(firstName: "zhan", lastName: "tianyou", age: 26)
    println Person(firstName: "liu", lastName: "fefie", age: 25)
    println CreditCard(number: '535-553-0555', areaCode: 2000)
}


fluentCreate()

//可以在不同的作用于使用@Newify注解，类或者方法，在创建DSL是，@Newify注解非常有用

//Singleton 用于实现单利 ，使用lazy可以设置为懒汉式

@Singleton(
        strict = false
)
class TheUnique {
    private TheUnique() {
        println "TheUnique created $this"
    }

    def hello() {
        println "hellow $this"
    }
}

TheUnique.instance.hello();
TheUnique.instance.hello();

//使用Singleton注解，会使目标类的构造方法变为私有的，但是Groovy并不区分私有的还是共有的，所以在Groovy内部还是可以使用new来创建
//类的对象，但是必须谨慎的使用该类，
TheUnique theUnique = new TheUnique()
theUnique.hello()

//InheritConstructors
class Father {
    Father(int a) {

    }

    Father(int a, int b) {

    }

    Father(int a, int b, int c) {

    }
}

@InheritConstructors//使用InheritConstructors可以帮助我们生成构造器
class Son extends Father {

}