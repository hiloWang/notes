package ztiany.chapter17

import groovy.json.JsonBuilder
import groovy.json.JsonSlurper

//--------------------
//处理Json
//--------------------

class Person{
    String first
    String last
    def sins
    def tools
}

john = new Person(first: "john", last: "Smith", sins: ['java', 'groovy'], tools: ['script': 'Groovy', 'test': 'Spock'])

bldr = new JsonBuilder(john)
writer = new StringWriter()
bldr.writeTo(writer)
println writer//{"sins":["java","groovy"],"first":"john","last":"Smith","tools":{"script":"Groovy","test":"Spock"}}

//当然可以对输出加以定制
bldr1 = new JsonBuilder(john)

bldr1{
    firstName john.first
    lastName john.last
    "special interest groups" john.sins
    "preferred tools"{
        numberOfTools john.tools.size()
        tools john.tools
    }
}
writer1 = new StringWriter()
bldr1.writeTo(writer1)
println writer1
//JsonBuilder可以从JavaBean、HashMap、列表生成JSON格式的输出。
//Json格式的输出内容保留在内存中，然后调用writeTo方法写入流中，如果希望在创建时就在流中可以使用StreamingJsonBuilder

bldr2 = new JsonBuilder(john)
def writer = new FileWriter('person.json')
bldr2.writeTo(writer)
writer.flush()
writer.close()


//--------------------
//使用JsonSlurper解析Json
//--------------------
sluper = new JsonSlurper()
def parse = sluper.parse(new FileReader('person.json'))
println "$parse.first , $parse.last , ${parse.sins.join(' , ')}"





