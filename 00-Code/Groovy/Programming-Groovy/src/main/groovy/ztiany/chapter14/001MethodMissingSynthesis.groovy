package ztiany.chapter14;


//--------------------
//使用methodMissing合成方法
//--------------------


class Person {

    def work() {
        "working ... ..."
    }

    def plays = ['Tennis', 'Volleyball', 'Basketball'];

    def methodMissing(String name, args) {
        System.out.println("methodMissing called for $name")
        def methodInList = plays.find { it == name.split('play')[1] }//分割方法名，取数组中的字符串
        if (methodInList) {
            "playing ${name.split('play')[1]}"
        } else {
            throw new MissingMethodException()
        }
    }
}

def jack = new Person()
println jack.work()
println jack.playTennis()
println jack.playVolleyball()
println jack.playBasketball()
/*
working ... ...
methodMissing called for playTennis
playing Tennis
methodMissing called for playVolleyball
playing Volleyball
methodMissing called for playBasketball
playing Basketball
 */
//上面用通过methodMissing的特性拦截了对不存在方法的调用
//同样，通过propertyMissing，可以拦截对不存在的属性的访问
//从调用者的角度来看，调用普通的方法和合成的方法病么有什么两样
//但是存在一个陷阱，重复的调用一个不存在的方法每次都需要处理会带来性能问题，所以需要把已经调用的方法缓存起来，这就是(拦截、缓存、调用)

class Person1 {

    def work() {
        "working ... ..."
    }

    def plays = ['Tennis', 'Volleyball', 'Basketball'];

    def methodMissing(String name, args) {
        System.out.println("methodMissing called for $name")
        def methodInList = plays.find { it == name.split('play')[1] }//分割方法名，取数组中的字符串
        if (methodInList) {
            def impl = {
                Object[] vars->
                    "playing ${name.split('play')[1]}"
            }
            Person1 instance = this;//这里为什么不能直接使用this呢？
            instance.metaClass."$name"=impl//这样就把方法给缓存起来了
            impl(args)

        } else {
            throw new MissingMethodException()
        }
    }
}

println "jack1-----------------"
def jack1 = new Person1()
println jack1.work()
println jack1.playTennis()
println jack1.playVolleyball()
println jack1.playBasketball()