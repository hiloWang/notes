
//--------------------
//给具体的实例合成方法
//--------------------


class Person4{
}

def emc = new ExpandoMetaClass(Person4)

emc.methodMissing={
    String name,args->
        "I am jack of all trades ... I can $name"
}

emc.initialize()
def jack = new Person4()
jack.metaClass = emc

println jack.sing()
println jack.paly()
println jack.fuck()
println jack.codeing()


