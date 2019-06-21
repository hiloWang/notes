package ztiany.chapter13
//--------------------
//使用ExpandoMetaClass注入方法
//--------------------

//--------------------
//注入类方法
//--------------------
//给Integer注入daysFromNow方法
Integer.metaClass.daysFromNow = {
    Calendar today = Calendar.getInstance()
    today.add(Calendar.DAY_OF_MONTH, delegate)
    today.time
}
println 4.daysFromNow()

//小窍门 在注入的方法前面加入get，在以后的方法中可以以属性的方法访问
Integer.metaClass.getDaysFromNow = {
    ->
    Calendar today = Calendar.getInstance()
    today.add(Calendar.DAY_OF_MONTH, (int) delegate)
    today.time
}
println 6.daysFromNow

//--------------------
//给多个类注入方法的方式
//--------------------

getPrettyName = {
    ->
    "prettyName---" + delegate
}
Integer.metaClass.getPrettyName = getPrettyName
Long.metaClass.getPrettyName = getPrettyName

println 4.prettyName
println 6L.prettyName

//或者在接口上注入方法，则所有实现者都拥有此方法
Number.metaClass.getPrettyName = getPrettyName
println 1000000000000L.prettyName

//--------------------
//注入静态方法
//--------------------
Integer.metaClass.'static'.isEven = {
    val ->
        val % 2 == 0

}

println Integer.isEven(5)
println Integer.isEven(6)

//--------------------
//注入构造器
//--------------------

//可以使用 << 来为一个类添加构造器，使用 << 来覆盖现有的构造器或者方法会导致异常
Integer.metaClass.constructor << {
    Calendar calendar ->
        new Integer(calendar.get(Calendar.DAY_OF_YEAR))
}
//如果想要替换(覆盖)一个原来的构造器可以使用=操作符


Integer.metaClass.constructor = {
    int val ->
        println '拦截了构造方法'
        //在覆盖的构造方法内仍可以使用反射来调用原来的实现
        constructor = Integer.class.getConstructor(Integer.TYPE)
        constructor.newInstance(val)
}

println new Integer(4)
println new Integer(Calendar.getInstance())

//--------------------
//注入一组方法
//--------------------

String.metaClass {

    parseInteger = { ->
        Integer.parseInt(delegate)
    }

    //静态方法
    'static' {
        isShorString = {
            String str ->
                str.length() < 4
        }
    }

    constructor = {
        String str ->
            println "拦截了String的构造方法"
            constructor = String.class.getConstructor(String.class)
            constructor.newInstance(str)
    }
}

println '213'.parseInteger()
println String.isShorString("3442323")
println new String("aaaBBB")

//--------------------
//向具体的示例中注入方法
//--------------------

//每一个实例都有一个MetaClass，向实例的MetaClass注入方法，可以防止注入的方法应用到类上


class Person {
    def play() {
        println "playing"
    }
}

def emc = new ExpandoMetaClass(Person)
emc.sing = {
    "sing... ob baby"
}
emc.initialize()
def jack = new Person()
def paul = new Person()

jack.metaClass = emc

println jack.sing()

try {
    println paul.sing()
} catch (e) {
    println  e
}

//还有更简单的方式

def jams = new Person()
jams.metaClass.sing = {
    "jams sing ob baby"
}
println jams.sing()

//也可以给对象注入一组方法

def jordan = new Person()
jordan.metaClass {

    playBasketball = {
        ->
        "飞人乔丹灌篮"
    }

    playBasetball = {
        ->
        "飞人乔丹打棒球"
    }
}

println jordan.playBasketball()
println jordan.playBasetball()

