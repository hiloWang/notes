package ztiany.chapter11;



//--------------------
//Groovy对象
//--------------------

//Groovy对象实现了groovy.lang.GroovyObject

class AGroovyClass {

}

println "a".getClass().interfaces//[interface java.io.Serializable, interface java.lang.Comparable, interface java.lang.CharSequence]
println new AGroovyClass().getClass().interfaces//[interface groovy.lang.GroovyObject]



//--------------------
//查询方法和属性
//--------------------
str = "hello"
methodName = "toUpperCase"
mehtodOfInterest = str.metaClass.getMetaMethod(methodName)
println mehtodOfInterest.invoke(str)

//str.metaClass.getMetaMethod()
//str.metaClass.getStaticMetaMethod()
//str.metaClass.getMetaProperty()
//str.metaClass.respondsTo()
//str.metaClass.hasProperty()

//--------------------
//动态的访问对象
//--------------------

def printInfo(obj) {

    usrRequestedProperty = 'bytes'
    usrRequestedMethod = 'toUpperCase'

    //动态访问属性的方式
    println obj[usrRequestedProperty]
    println obj."$usrRequestedProperty"
    //动态调用方法的方式
    println obj."$usrRequestedMethod"()
    println obj.invokeMethod(usrRequestedMethod, null)

}

printInfo("hello")

//访问对象的所有属性
"hello".properties.each {
    println it
}



