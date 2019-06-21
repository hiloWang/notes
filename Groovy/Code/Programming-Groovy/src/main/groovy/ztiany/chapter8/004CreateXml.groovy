package ztiany.chapter8

import groovy.xml.StreamingMarkupBuilder

//--------------------
//创建xml
//--------------------

//1 使用GString的嵌入能力创建xml前面 已经学习过了

//2 使用MarkupBuilder或者StreamingMarkupBuilder示例：(具体参见第17.1章)

langs = ['c++':'Stroustrup' ,'java':'Gosling', 'Lisp':'McCarthy']

/*
 that mkp is a special namespace used to escape away from the normal building mode of the builder
 and get access to helper markup methods 'yield', 'pi', 'comment', 'out', 'namespaces', 'xmlDeclaration' and 'yieldUnescaped'.
 Note that tab, newline and carriage return characters are escaped within attributes, i.e. will become and respectively
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
new File('test3.xml').withWriter {
    it << xmlDocument
}
/*
<languages xmlns:computer='Computer'>
    <!--Created using StreamingMarkupBuilder-->
    <computer:language name='c++'>
        <author>Stroustrup</author>
    </computer:language>
    <computer:language name='java'>
        <author>Gosling</author>
    </computer:language>
    <computer:language name='Lisp'>
        <author>McCarthy</author>
    </computer:language>
</languages>

 */


