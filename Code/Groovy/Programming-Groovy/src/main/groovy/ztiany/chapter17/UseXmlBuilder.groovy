package ztiany.chapter17

import groovy.xml.MarkupBuilder
import groovy.xml.StreamingMarkupBuilder

//--------------------
//创建XML
//--------------------

/*
调用languages方法表示创建一个languages接口，附上的闭包提供了一个内部的上下文，闭包内的任何方法调用都会认为是上一个节点的子节点。
调用方法中传入单个String表示输出一个节点，传入Map参数表示为节点的属性：
 */
bldr1 = new MarkupBuilder()

bldr1.languages {
    language(name: "c++") {
        author('Stroustrup')
    }
    language(name: "java") {
        author('Gosling')
    }
    language(name: "Lisp") {
        author('McCarthy')
    }
}
println ""

//也可以在MarkupBuilder创建时指定要给Writer
langs = ['c++':'Stroustrup' ,'java':'Gosling', 'Lisp':'McCarthy']
StringWriter stringWriter = new StringWriter()

bldr2 = new MarkupBuilder(stringWriter)
bldr2.languages {
    language(name: "c++") {
        author('Stroustrup')
    }
    language(name: "java") {
        author('Gosling')
    }
    language(name: "Lisp") {
        author('McCarthy')
    }
}
println stringWriter.toString()



/*
MarkupBuilder适用于中小型的xml，如果文件够大(若干兆字节)，可以string用StreamingMarkupBuilder，它的内存占用可能会更好一些
 */

xmlDocument = new StreamingMarkupBuilder().bind {
    println mkp//.BaseMarkupBuilder$Document@4690b489 mkp是一个特殊的命名，指向Document
    mkp.xmlDeclaration()//声明xml 文件 <?xml version='1.0'?>
    mkp.declareNamespace(computer:"Computer")//声明命名空间
    languages{//声明languages节点
        comment  << "Created using StreamingMarkupBuilder"//注释
        langs.each{
            key,value->
                computer.language(name:key){//使用computer命名空间 创建language节点，并设置属性name为key
                    author(value)//在language节点在创建author节点，把value作为其内容
                }
        }
    }
}

println xmlDocument





