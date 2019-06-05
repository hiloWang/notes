package ztiany.chapter7;

//io的扩展

/*
Java中file也加入了很多方面的方法，其中eachFile和eachDir这样的方法可以接受闭包，

Java中读取File的文本显得过于繁复
Groovy通过向BufferedReader、InputStream、File添加了一个text属性用于一次新读取file或者流中的文本，使之简单的了许多
 */
println new File("test.md").text//读取文档中的文本就是这么简单
//如果不想一次性读取整个文本，可以使用eachLine进行逐行读取
new File("test.md").eachLine {
    println it
}
//还可以对文本进行过滤
println new File("test.md").filterLine {
    String s->
        s.startsWith("#")
}
//如果想使用完毕时自动刷新并关闭流，可以使用withStream方法，该方法会创建一个InputStream的实例作为参数发送给闭包,就像之前学习的闭包的特性一样，流会自动关闭

//InputStream的withReader会创建一个BufferedReader，并将其作为参数传给闭包，也可以调用newReader方法创建一个BufferedReader

new File('test.md').withWriter {
    println it//BufferedWriter
    it << "ni hao a"
}


//java.util的扩展

//List Set  SortedMap 都加入了asImmutable方法，用于获取对应的一个不可变对象，
//asSynchronized 方法用于创建线程安全的实例
//Iterator支持的inject方法前面学习过
//Timer.runAfter方法接受一个闭包，改闭包将在给定的(毫秒)时间后运行

















