package ztiany.chapter17




/**
 *
 * <br/>    功能描述：
 * <br/>    Email     : ztiany3@gmail.com
 * @author Ztiany
 * @see
 * @since 1.0
 */
class ToDoBuilder {
        def level = 0 //代表任务的层级嵌套深度
        def result = new StringWriter()//存储生成的DSL

    def build(Closure closure) {
        result << "todo:\n"
        closure.delegate = this
        closure()
        println result
    }

    def methodMissing(String name,args) {
        handle(name,args)
    }

    def propertyMissing(String name) {
        Object[] empty = []
        handle(name,empty)
    }

    def handle(String name, args) {
        level++
        level.times {
           result << "  "
        }

        result << placeXifStatusIsDone(args)
        result << name.replaceAll("_"," ")
        result << printParameters(args)
        result << "\n"

        if(args.length > 0 && args[-1] instanceof Closure){
            def theClosure = args[-1]
            theClosure.delegate = this
            theClosure()
        }
        level--
    }

    def placeXifStatusIsDone(args) {
        args.length > 0 && args[0] instanceof Map && args[0]['status'] == 'done'? "x  ":'-  '
    }

    def printParameters(args) {
        def values = ""
        if(args.length > 0 &&  (args[0] instanceof Map) ){
            values += '['
            def count = 0
            args[0].each {
                key,value->
                    count++
                    values += (count > 1? " ":"")
                    values+= "$key : $value"
            }
            values += ']'
        }
        values
    }
}





//使用ToDoBuilder处理下面层次结构：
bldr = new ToDoBuilder()
bldr.build{
       Prepare_Vacation(start: '02/15',end : '02/22'){

           Reserve_Flight(on:'01/01',status:'done')
           Reserve_Hotel(on:'01/02')
           Reserve_Car(on:'01/02')

       }

        Buy_New_Mac{

            Install_QuickSilver
            Install_TextMate

            Install_Groovy{
                Run_all_tests
            }
        }
}
/*
结果为：

todo:
  -  Prepare Vacation[start : 02/15 end : 02/22]
    x  Reserve Flight[on : 01/01 status : done]
    -  Reserve Hotel[on : 01/02]
    -  Reserve Car[on : 01/02]
  -  Buy New Mac
    -  Install QuickSilver
    -  Install TextMate
    -  Install Groovy
      -  Run all tests
 */

/*
在上述用于待办事项列表的DSL中，使用ToDoBuilder来处理，使用methodMissing和propertyMissing方法来拦截不存在的方法和属性名，位置知道的方法时build。

几乎全部都是标准字节的Groovy代码，而且很好的应用了元编程，当调用不存在的方法和属性名时，就假单它时一个条目。

当面对嵌套层次较深，而且大量使用Map和普通参数的复杂情况，使用BuilderSupport较好。
 */


