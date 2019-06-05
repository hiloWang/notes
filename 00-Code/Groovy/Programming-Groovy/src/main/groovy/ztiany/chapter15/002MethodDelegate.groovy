package ztiany.chapter15

//--------------------
//方法委托：汇总练习
//--------------------

//繁复的实习委托

class Worker {
    def simpleWork1(spec) {
        println "Worker does work1 with space $spec"
    }

    def simpleWork2(spec) {
        println "Worker does work2 with space $spec"
    }
}

class Expert {
    def advanceWork1(spec) {
        println "Expert does work1 with space $spec"
    }

    def advanceWork2(scope , spec) {
        println "Expert does work2 with scope $scope space $spec"
    }
}

class Manager {
    def worker = new Worker()
    def expert = new Expert()

    def schedule() {
        println "scheduling ......"
    }

    def methodMissing(String name, args) {
        println "intercept call $name"
        def delegateTo = null
        if (name.startsWith('simple')) {
            delegateTo = worker
        }
        if (name.startsWith('advance')) {
            delegateTo = expert
        }
        print "delegateTO $delegateTo   "
        def to = delegateTo?.metaClass?.respondsTo(delegateTo, name, args)
        println "to $to"
        if (to) {
            Manager instance = this
            instance.metaClass."$name" = {
                Object[] vars ->
                    delegateTo.invokeMethod(name, args)
            }
            this."$name"(args)
        } else {
            throw new MissingMethodException(name, Manager.class, args)
        }
    }
}

def peter = new Manager()
peter.schedule()
peter.simpleWork1("fast")
peter.simpleWork2("quality")
peter.advanceWork1("prototype ")
peter.advanceWork2('product',"quality")

//--------------------
//Groovy的方式
//--------------------
println "Groovy -----------------Way------------------------"

class  Manager2{
    {delegateClassTo Worker,Expert,GregorianCalendar}

    def schedule() {
        println "scheduling ..."
    }
}

/*
这段代码轻松的实现了方法委托，实现了上面delegateClassTo方法。当调用到为实现的方法时就会被路由到methodMissing上，
然后会根据delegateClassTo传入的Class对象，进行实例化，查看Class的实例中是否有能处理该方法的实例。
 */
Object.metaClass.delegateClassTo={
    Class...classes->
        def objectOfDelegates = classes.collect{
            it.newInstance()
        }
        delegate.metaClass.methodMissing={
            String name,args->
                println "intercept called $name"
                def delegateTo = objectOfDelegates.find{
                    it.metaClass.respondsTo(it,name,args)
                }
                if (delegateTo) {
                    delegate.metaClass."$name"={
                        java.lang.Object[] varArgs->
                            delegateTo.invokeMethod(name,args)
                    }
                    delegate."$name"(args)
                }else {
                    throw new MissingMethodException(name, delegate.getClass(), args)
                }
        }
}


def manager2 = new Manager2()
manager2.schedule()
manager2.simpleWork1("fast")
manager2.simpleWork2("quality")
manager2.advanceWork1("prototype ")
manager2.advanceWork2('product','quality')







