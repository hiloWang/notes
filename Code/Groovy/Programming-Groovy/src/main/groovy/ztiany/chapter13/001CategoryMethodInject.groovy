package ztiany.chapter13
//--------------------
//使用分类注入方法
//--------------------
class StringUtils {

    def static toSSN(self) {//如果只希望为String提供toSSN方法则声明此方法的参数类型为String
        if (self.size() == 9) {
            "${self[0..2]}--${self[3..4]}--${self[5..8]}"
        }
    }
}

use(StringUtils) {
    println "123456789".toSSN()
    println new StringBuffer("987654321").toSSN()
}
try {
    "123456789".toSSN()
} catch (e) {
    println e
}


//--------------------
//使用Category注入方法
//--------------------
@Category(String )//指定为String提供方法注入，如果想要为所有的类提共此方法注入，则声明为Object
class AnnotatedStringUtils {

    def  toSSN() {//如果只希望为String提供toSSN方法则声明此方法的参数类型为String
        if (this.size() == 9) {
            "${this[0..2]}--${this[3..4]}--${this[5..8]}"
        }
    }
}
use(AnnotatedStringUtils){
    println "963258741".toSSN()
}
/*
Groovy脚本把use方法路由到了GroovyCategorySupport类的use方法。
 */



//--------------------
//传入闭包
//--------------------
class FindUtils{
    def static extractOnly(String self, closure) {
        def result = ''
        self.each {
            if (closure.call(it)) {
                result += it
            }
        }
        result
    }
}

use(FindUtils){
    println "123698547".extractOnly{
        it=='4'||it=='5'
    }
}

//内置分类：Groovy提供了很多方法我们使用的分类，比如DOMCategory用于处理XML，支持了GPath
//groovy.time.TimeCategory
//groovy.servlet.ServletCategory
//groovy.xml.dom.DOMCategory



//--------------------
//传入类
//--------------------
use(StringUtils, FindUtils){
    println "123698547".extractOnly{
        it=='5'||it=='0'
    }
    println "123698547".toSSN()
}

//如果多个类中存在重复的方法，则参数列表中最后一个类提供的方法优先级最高
//use还可以嵌套使用，即use中使用use，此时内部的分类优先级较高



//--------------------
//拦截方法
//--------------------
class Helper{
    def static toString(String self) {
        //这里的toString用于拦截self的toString，所以这里调用metaClass的toString方法
        def method = self.metaClass.metaMethods.find{it.name == 'toString'}
        '!!'+method.invoke(self,null)+'!!'
    }
}

use(Helper){
    println "123".toString()
}
//使用分类拦截方法并不优雅，因此分类应该用于注入方法



















