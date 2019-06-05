package ztiany.chapter8

//--------------------
//使用XmlSlurper
//--------------------

/*
对于较大的文档，使用XmlParser对内存的压力是比较大的，而使用XmlSlurper可以处理较大的xml
其使用上与XmlParser类似
 */

languages = new XmlSlurper().parse('test1.xml')
languages.language.each {//还是有一定的区别的，要先通过.language获取所有的language
    println "${it.@name} author by ${it.author[0].text()}"

}


//处理命名空间

languagesNs = new XmlSlurper().parse('test2.xml')
.declareNamespace(human:'natural')//declareNamespace方法用于声明一个命名空间，语法为    human:'natural' 其中human可以随便取，用于标识natural命名空间
println languagesNs.language.collect{it.@name}.join(' , ')
println languagesNs.'human:language'.collect{it.@name}.join(' , ')//.'human:language'用于获取human对应命名空间的所有元素



