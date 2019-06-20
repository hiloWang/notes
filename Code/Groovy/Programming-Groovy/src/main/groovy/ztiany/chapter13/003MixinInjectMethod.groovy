package ztiany.chapter13

//--------------------
//使用Mixin注入方法
//--------------------

class Friend {
    def listen() {
        "$name listening as a friend"
    }
}
/*
使用@Mixin注解语法注入方法:
将Mixin注入到APerson中，Mixin注解会将作为参数的类中的方法添加到被注解的类中，
Mixin的方式非常优雅，但是如果也有一定的限制，这种方式只能由类的作者使用
 */
@Mixin(Friend)
class APerson {
    String firstName
    String lastName

    String getName() {
        "$firstName $lastName"
    }
}

def john = new APerson(firstName: "Zhan",lastName: 'Tianyou')
println john.listen()//Zhan Tianyou listening as a friend

//另外一种方式是使用静态代码块注入mixin
class BPerson{
    static {
        mixin Friend
    }

    String getName() {
        "BPerson"
    }
}

println new BPerson().listen()//BPerson listening as a friend





//--------------------
//动态的混入方法
//--------------------
//向Dog中混入方法

class Dog{
    String name;
}
Dog.mixin Friend//调用Dog类的mixin方法，混入Friend
//现在Dog以及拥有了Listen方法了
println  new Dog(name: "Buddy").listen()//Buddy listening as a friend



//也可向特定类的实例中混入方法，这样混入的方法只在该实例上：
class Cat {
    String name
}

try {
        new Cat(name: "Rude").listen()// No signature of method: ztiany.chapter13.Cat.listen()
} catch (e) {
    println e
}
tom = new Cat(name: "tom")
tom.metaClass.mixin Friend
println tom.listen()//tom listening as a friend










