package ztiany.chapter14;


//--------------------
//使用GroovyInterceptable合成方法
//--------------------


class Person2 implements GroovyInterceptable{

    def work() {
        "working ... ..."
    }

    def plays = ['Tennis', 'Volleyball', 'Basketball'];

    @Override
    def invokeMethod(String name, Object args) {
        System.out.println("intercept method $name")
        def method = metaClass.getMetaMethod(name, args)
        if (method) {
            method.invoke(this,args)
        }else {
            metaClass.invokeMethod(this,name,args)//注入三个参数的方法，两个参数的方法就是GroovyObject的方法了
        }
    }


    def methodMissing(String name, args) {
        System.out.println("methodMissing called for $name")
        def methodInList = plays.find { it == name.split('play')[1] }//分割方法名，取数组中的字符串
        if (methodInList) {
            def impl = {
                Object[] vars->
                    "playing ${name.split('play')[1]}"
            }
            Person2 instance = this;//这里为什么不能直接使用this呢？
            instance.metaClass."$name"=impl//这样就把方法给缓存起来了
            impl(args)

        } else {
            throw new MissingMethodException()
        }
    }


}

def jack = new Person2()
println jack.work()
println jack.playTennis()
println jack.playVolleyball()
println jack.playBasketball()