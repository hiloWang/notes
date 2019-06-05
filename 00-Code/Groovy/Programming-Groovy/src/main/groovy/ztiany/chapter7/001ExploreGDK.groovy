package ztiany.chapter7


//--------------------
//使用Object类的扩展
//--------------------
/*
Groovy在Collection上添加了each，find，collect等方法，其实不仅Collection可以使用这些方法，其任何对象都是可以使用的，
这为我们以类似的方式使用不同的对象和集合体提供了一直的api，

而Object上也添加了一些与集合无关的方法，查看GKD文档可以更加清晰的了解
 */

// dump 和 inspect方法

//如果想知道一个实例包含哪些内容，可以在运行时使用dump方法
str = 'hello'
println str
println str.dump()//<java.lang.String@5e918d2 value=hello hash=99162322>

//inspect方法旨在说明创建一个对象需要提供什么,如果类没有实现该方法，则简单的返回toString方法
println BigDecimal.inspect()


//--------------------
//使用上下文with方法
//--------------------

//with作用域范围内调用任何方法都会被定向到上下文你对象
list = [1,3]
list.add(3)
println list.size()
println list.contains(3)
//使用with省略调用者
list.with {
    add(5)
    println size()
}
//with方法使用的是闭包的delegate特性
list.with {
    println "this is $this"
    println "owner is $owner"
    println "delegate is $delegate"
}
/*
this is ztiany.chapter7._001ExploreGDK@399f45b1
owner is ztiany.chapter7._001ExploreGDK@399f45b1
delegate is [1, 3, 3, 5]

 */

//使用sleep方法

thread = Thread.start {
    println "thread start"
    startTime = System.nanoTime()
    new Object().sleep(2000)
    endTime = System.nanoTime()
    println "thread done in ${(endTime -startTime)/10**9} seconds"
}
new Object().sleep(300)
println "let interrupt the thread"
thread.interrupt()
thread.join()


//在Object方法上调用sleep与使用Java提供的sleep方法不同的是
//如果发生interruptedException，前者会压制，如果我们确实想被中断，也不必受try/catch块之苦
def playWithSleep(boolean flag) {
    thread = Thread.start {
        println "thread start"
        startTime = System.nanoTime()
        new Object().sleep(2000){
            flag//返回true表示希望被interruptedException中断
        }
        endTime = System.nanoTime()
        println "playWithSleep flag=$flag thread done in ${(endTime -startTime)/10**9} seconds"
    }
    thread.interrupt()
    thread.join()
}
playWithSleep(true)
playWithSleep(false)


//间接访问属性
//当我们需要访问对象的位置属性时，可以使用[]操作符动态的访问属性，该操作符映射到的方法是Groovy添加的getAt方法
//如果该操作符用于对象的左侧，它映射到的是putAt方法

class Car{
    int miles,fuelLevel
}


car = new Car(fuelLevel: 3,miles: 32)
properties =['miles', 'fuelLevel']//假设这是不确定的

properties.each {name->
    println "$name = ${car[name]}"
}

car[properties[1]] = 2333
println car.fuelLevel
/*
miles = 32
fuelLevel = 3
2333
 */


//间接的调用方法
//Groovy为每类都添加了invokeMethod方法，当需要调用一个未知的方法时，只需要简单的调用invokeMethod方法，而不是
//向Java那样使用反射，还要处理各种异常
class Person{
    def walk() {
        println "walking..."
    }

    def walk(int miles) {
        println "wrlking $miles"
    }
}

peter = new Person()
peter.invokeMethod('walk',null)
peter.invokeMethod('walk',21)
/*
walking...
wrlking 21
 */















