package ztiany.chapter12;

//--------------------
//使用GroovyInterceptable拦截方法
//--------------------

//适用于拦截自己编写的类

class Car implements GroovyInterceptable {
    def check() {
        System.out.println("check called")//这里使用的时System.out.println，因为println时Groovy在Object上注入的方法，使用它会被拦截掉
    }

    def start() {
        System.out.println("start called")
    }

    def end() {
        System.out.println("end called")
    }

    @Override
    def invokeMethod(String name, Object args) {
        System.out.println("call $name....")
        if (name != "check") {
            System.out.println("running filter....")
            this.metaClass.getMetaMethod("check").invoke(this, null)
        }

        def validMethod = Car.metaClass.getMetaMethod(name, args)
        if (validMethod != null) {
            validMethod.invoke(this, args)
        } else {
            Car.metaClass.invokeMethod(this, name, args)
        }

    }
}

def car = new Car()
car.start()
car.end()
car.check()
try {
    car.speed()
} catch (e) {
    println e
}
