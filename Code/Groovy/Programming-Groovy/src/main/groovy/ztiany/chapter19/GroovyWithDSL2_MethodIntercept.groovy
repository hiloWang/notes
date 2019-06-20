package ztiany.chapter19

//--------------------
//方法拦截与DSL
//--------------------

/*
不用Pizza类也可以实现订购pizza
在orderPizza.dsl中定义订购pizza的逻辑
 */


def dslDef = new File("GroovyPizzaDSL.groovy").text
def dsl = new File('orderPizza.dsl').text

def script = """
    ${dslDef}
       acceptOrder{
            ${dsl}
        }
"""

println script

new GroovyShell().evaluate(script)



