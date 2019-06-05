package ztiany.chapter11


//--------------------
//测试MetaClass与对象本身方法调用的优先级
//--------------------
class A {
    def ma() {
        println "mb origin  call"
    }
}

A.metaClass.ma = { ->
    println "ma call"
}

A.metaClass.getMetaMethod("ma").invoke(new A())//ma call