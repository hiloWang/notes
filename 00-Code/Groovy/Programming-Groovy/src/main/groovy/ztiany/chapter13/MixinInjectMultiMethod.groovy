package ztiany.chapter13

import groovy.transform.ToString


//--------------------
//在类中使用多个Mixin
//--------------------

/*
当混入多个类时，所有这些类的方法在目标类中都是可用的，默认情况下多个方法会因为冲突而被隐藏，
也就是说，当作为Mixin的两个或者多个存在相同名字、参数签名也相同时，最后加入到Mixin的方法优先级较高。

通过编程实现，以方法调用链的方式将这些方法连接起来，反而可以让他们合作

注意：这里的实现调用链是核心！
 */

//一般应用需要不同类型的输出目标，可能是文件、套接字、web服务等，将之抽象为Writer类
abstract class Writer {
    abstract void write(String message)
}
//StringWriter是这个类的一个具体实现，在write方法中将给定点的消息写入到一个StringBuilder
@ToString
class StringWriter extends Writer {
    def target = new StringBuilder()

    @Override
    void write(String message) {
        target.append(message)
    }
}

def writeStuff(writer) {
    writer.write("this is stupid")
    println writer
}

/*
 *  给theWriter混入filters
 */

def create(theWriter, Object[] filters = []) {
    def instance = theWriter.newInstance()
    filters.each { filter ->
        instance.metaClass.mixin filter
    }
    instance
}

writeStuff(create(StringWriter))

//一个过滤器
class UppercaseFilter {
    void write(String message) {
        def allUpper = message.toUpperCase()
        invokeOnPreviousMixin(metaClass, 'write', allUpper)
    }
}

Object.metaClass.invokeOnPreviousMixin = {


    MetaClass currentMixinMetaClass, String method, Object[] args ->

        println "currentMixinMetaClass $currentMixinMetaClass.delegate.theClass"

        def previousMixin = delegate.getClass()//这里的delegate是目标实例，即Filter的某个实例


        //mixedIn属性：用于为实例保存有序的mixin实例
        for (mixin in mixedIn.mixinClasses) {//mixedIn.mixinClasses 遍历所有的mixin

            //mixin的类型为：MixinInMetaClass
            //MixinInMetaClass.mixinClass：CachedClass
            //CachedClass.theClass：返回原来的Class：原来的class即Filter对象的Class

            if (mixin.mixinClass.theClass == currentMixinMetaClass.delegate.theClass) {
//当找到传入参数的mixin时跳出
                break
            }
            previousMixin = mixin.mixinClass.theClass
        }

        println "mixedIn[previousMixin] ${mixedIn[previousMixin]}"//ExpandoMetaClass$MixedInAccessor

        mixedIn[previousMixin]."$method"(*args)//这里的*args 为展开操作符
}

writeStuff(create(StringWriter, UppercaseFilter))

//过滤 stupid的Filter

class ProfanityFilter {
    void write(String message) {
        def filtered = message.replaceAll('stupid', 's******')
        invokeOnPreviousMixin(metaClass, 'write', filtered)
    }
}

writeStuff(create(StringWriter, ProfanityFilter))
writeStuff(create(StringWriter, UppercaseFilter, ProfanityFilter))//这里混入的顺序不一样，所以结果也不一样
writeStuff(create(StringWriter, ProfanityFilter, UppercaseFilter))



