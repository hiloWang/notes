package ztiany.chapter8

import groovy.xml.DOMBuilder
import groovy.xml.dom.DOMCategory

//--------------------
//解析xml
//--------------------

/*使用Groovy的分类可以在类上定义动态的方法，其中一个分类DOMCategory(DOM:Document Object Model)可以处理文档对象模型
DOMCategory可以通过GPath(Groovy path extension)：
 */
document = DOMBuilder.parse(new FileReader('test1.xml'))
rootElement = document.documentElement//这里的rootElement并不是languages

println rootElement.childNodes.length
println rootElement.getClass().name

use(DOMCategory){
    println "language and authors "
    languages = rootElement.language//而languages = rootElement.language 可以理解为从rootElement获取所有的language，即languages
    println languages.author//[[author: null], [author: null], [author: null]] 从打印的信息可以看出 languages代表的是所有的language

    languages.each {
        language->
        println "${language.'@name'} authored by ${language.author[0].text()}"//'@name'语法用于访问属性，author[0]表示访问第一个子节点，text()表示获取节点里的内容
    }

    def languageByAuthor = {
        authorName ->
            languages.findAll{it.author[0].text() == authorName}.collect{
                it.'@name'
            }.join(' , ')//获取所有作者为authorName的 书名的文本
    }
    println "Language By Gosling : "+languageByAuthor('Gosling')
}
/*
DOMCategory结合GPath可以很方便的处理xml
要使用DOMCategory必须把代码放在use块内，
 */

/*
GPath是什么：
与XPath类似，GPath可以帮助导航对象Plain( Old Java Object 普通java对象)和( Old Groovy Object)和xml层次结构
使用 . 可以遍历层次结构， 例如car.engine.power这种写法会帮助我们通过要给car实例的getEngine方法访问engine属性，然后通过
或取得的engine实例的getPowder方法方法该实例的powder属性

即 . 用来获取子元素
而 car.‘@year' 或者 car.@year 用于获取car节点上的year属性，而不是子节点
 */














