package ztiany.chapter8

//--------------------
//使用XMLParser
//--------------------

/*
使用XMLParser,利用Groovy的动态类型和元编程能力，可以直接使用名字访问文档中的成员，例如使用it.author[0]访问一个作者的名字
 */

languages = new XmlParser().parse('test1.xml')
println "Language and Author"
languages.each {
    println "${it.@name} author by ${it.author[0].text()}"
}

def languageByAuthor = {
    authorName->
        languages.findAll{
            it.author[0].text() == authorName
        }.collect{
            it.@name
        }.join(' , ')
}

println languageByAuthor("Stroustrup")
