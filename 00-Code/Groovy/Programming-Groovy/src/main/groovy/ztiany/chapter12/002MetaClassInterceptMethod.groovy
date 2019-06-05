package ztiany.chapter12;

//--------------------
//使用MetaClass拦截方法
//--------------------

class Car1 {

    def check() {
        System.out.println("check called")
    }

    def start() {
        System.out.println("start called")
    }

    def end() {
        System.out.println("end called")
    }
}

Car1.metaClass.invokeMethod = {
    String name, args ->

        System.out.println("call $name....")
        if (name != "check") {
            System.out.println("running filter....")
            delegate.metaClass.getMetaMethod("check").invoke(delegate, null)
        }

        def validMethod = Car1.metaClass.getMetaMethod(name, args)
        if (validMethod != null) {
            validMethod.invoke(delegate, args)
        } else {
            //这里因为已经在invokeMethod中了，所以使用invokeMissingMethod，而不是递归的调用invokeMethod
            Car1.metaClass.invokeMissingMethod(delegate, name, args)
        }
}

def car1 = new Car1()
car1.start()
car1.end()
car1.check()
try {
    car1.speed()
} catch (e) {
    println e
}



//--------------------
//测试拦截Integer的方法
//--------------------


/*
如果感兴趣的只是拦截对不存在的方法的调用，那应该使用methodMissing来代替invokeMethod，可以
在metaClass上同时提供invokeMethod和methodMissing，invokeMethod有优先于methodMissing处理，
而调用metaClass的invokeMissingMethod其实就是调用methodMissing方法
 */

Integer.metaClass.invokeMethod = {
    String name, args ->
        System.out.println("call $name on $delegate")

        def validMethod = Integer.metaClass.getMetaMethod(name, args)
        if (validMethod == null) {
            Integer.metaClass.invokeMissingMethod(delegate, name, args)//调用methodMissing
        } else {
            System.out.println(" running pre filter")
            resut = validMethod.invoke(delegate, args)
            System.out.println(" running post filter")
            resut
        }
}

Integer.metaClass.methodMissing = {
    String name, args ->
        System.out.println("methodMissing $delegate")
}

println 4.floatValue()
println 5.intValue()
try {
    21.method()
} catch (e1) {
    println e1
}



//--------------------
//ExpandoMetaClass
//--------------------
println Integer.metaClass.getClass().name//groovy.lang.ExpandoMetaClass

/*
ExpandoMetaClass是MetaClass的一个实现，也是Groovy中实现动态行为的关键类之一，默认情况下Groovy并没有使用ExpandoMetaClass

 */

println String.metaClass.getClass().name//org.codehaus.groovy.runtime.HandleMetaClass
String.metaClass.toString = {
    "non to string"
}
println String.metaClass.getClass().name//groovy.lang.ExpandoMetaClass
