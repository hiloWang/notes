package ztiany.chapter14;


//--------------------
//使用ExpandoMetaClass合成方法
//--------------------


class Person3{

    def work() {
        "working ... ..."
    }
}


def plays = ['Tennis', 'Volleyball', 'Basketball'];


Person3.metaClass.methodMissing = {
    String name, args->
        System.out.println("methodMissing called for $name")
        def methodInList = plays.find { it == name.split('play')[1] }//分割方法名，取数组中的字符串
        if (methodInList) {
            def impl = {
                Object[] vars->
                    "playing ${name.split('play')[1]}"
            }
            Person3.metaClass."$name"=impl//这样就把方法给缓存起来了
            impl(args)
        } else {
            throw new MissingMethodException()
        }
}






def jack = new Person3()
println jack.work()
println jack.playTennis()
println jack.playVolleyball()
println jack.playBasketball()