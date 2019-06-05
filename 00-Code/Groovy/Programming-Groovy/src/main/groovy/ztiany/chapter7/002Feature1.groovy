package ztiany.chapter7;

//--------------------
//其他扩展
//--------------------
//更详细的GDK所做的扩展，参考GDK文档，这里介绍的只是一个子集

//数组的扩展  所有的数据都可以使用range类型进行索引
arr = [3, 4, 6, 7, 5, 2, 2, 2, 51, 99921, 65, 21, 12] as int[]
println arr[1..4]

//java.lang的扩展
//对于基本量行的的包装类型，有一个值得注意的地方，那就是重载操作符，比如
// plus : +, next:++
//Number上加上了upto、downto、step方法

/*
Process类提供了访问stdin，stdout，stderr的命令，分别对应out,in,err属性
还有一个text属性，可以为我们提供完整的标准输出或者来进程的响应，如果想一次性读取读取完整的错误，可以在
进程实例上使用err.text，使用<<操作符可以以管道的方式连接到一个进程中，(管道：在Unix系统中用于将一个进程的输出链接到另一个进程的输入)
*/

def execute = 'cmd'.execute()//执行cmd获取一次进程

execute.out.withWriter {
//使用out的withWriter方法，withWriter方法会将OutputStreamWriter附到OutputStream上，并传给闭包，就像前面学习的一样，闭包或自动关闭流
    it << "dir\r\n"     //输入命令
}
println execute.in.text

/*
如果想将命令行参数发送给进程，有两个选择：把参数格式化为一个字符串，扩展创建一个参数的String数组，String[]也支持execute方法，其第一个元素
会被当作命令，其他元素作为参数，作为替代list也可以使用execute方法
 */


String[] command = ['groovy', '-e', ' "println \'Groovy\' "   ']
println "calling ${command.join('  ')}"
println command.join(' ').execute().text
/*
calling groovy  -e   "println 'Groovy' "
Groovy
 */

//线程相关 Thread的start和startDaemon方法可以快速的启动线程/守护线程
Thread.start {
    new FileWriter("test.md").withWriter {
        it << '# 我是一个线程'
        it << System.getProperty("line.separator", "\n")
        it << '- 我是一个线程'
        it << '- 我是一个线程'
    }

}



