package ztiany.chapter17

/*
TodoBuilderWithSupport扩展了BuilderSupport。
BuilderSupport提供了setParent方法和重载版本的createNode方法，还有一个可选的nodeCompleted方法

setParent 用于让生成器的作者直到当前所处理的节点的父节点
重载版本的createNode方法用于创建节点，不管其返回什么参数，都将视为节点，而生成器支持将其一个参数发送给nodeCompleted

关于重载的createNode方法，在DSL中如果传入Map+闭包，者调用的是createNode(Object name, Map attributes) 方法，此时闭包被当作一个方法而不是参数。


BuilderSupport不会像处理确实的方法那样处理确实的属性。然而仍然可以使用propertyMissing方法来处理这些情况。



 */

class TodoBuilderWithSupport extends BuilderSupport {

    int level = 0;
    def result = new StringWriter()


    @Override
    void setParent(Object parent, Object child) {
        println "${parent.getClass()}    ---    ${child.getClass()}"
    }

    @Override
    def createNode(Object name) {//没有参数的方法
        if (name == ("build")) {
            result << "todo:\n"
            'buildnode'
        } else {
            handleName(name, [:])
        }
    }

    @Override
    def createNode(Object name, Object value) {
        throw new Exception("invalid format")
    }

    @Override
    def createNode(Object name, Map attributes) {//map 参数的方法
        handleName(name, attributes)
    }

    @Override
    def createNode(Object name, Map attributes, Object value) {
        throw new Exception("invalid format")
    }

    @Override
    void nodeCompleted(Object parent, Object node) {
        level--
        if (node == "buildnode") {
            println result
        }
    }

    def propertyMissing(String name) {
        handleName(name, [:])
        level--//因为这里不会调用nodeCompleted方法，所以需要手动 减去层级
    }


    def handleName(String name, args) {
        level++
        level.times {
            result << "  "
        }
        result << placeXifStatusIsDone(args)
        result << name.replaceAll("_", " ")
        result << printParameters(args)
        result << "\n"
        name
    }

    def placeXifStatusIsDone(args) {
        args['status'] == 'done' ? "x  " : '-  '
    }

    def printParameters(args) {
        def values = ""
        if (args.size() > 0 ) {
            values += '['
            def count = 0
            args.each {
                key, value ->
                    count++
                    values += (count > 1 ? " " : "")
                    values += "$key : $value"
            }
            values += ']'
        }
        values
    }

}


builder = new TodoBuilderWithSupport()

builder.build {
    Prepare_Vacation(start: '02/15', end: '02/22') {//map

        Reserve_Flight(on: '01/01', status: 'done')//map
        Reserve_Hotel(on: '01/02')//map
        Reserve_Car(on: '01/02')//map

    }

    Buy_New_Mac {

        Install_QuickSilver //属性
        Install_TextMate  //属性

        Install_Groovy {   //闭包
            Run_all_tests   //属性
        }
    }
}