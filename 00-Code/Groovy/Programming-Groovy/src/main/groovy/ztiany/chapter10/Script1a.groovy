package ztiany.chapter10

println "in script1"
name = "ztiany"//name不能用def修饰

shell = new GroovyShell(binding)

def evaluate = shell.evaluate(new File('script2a.groovy'))//evaluate 求..的值

println evaluate


//如果不希望把影响当前的binding，则创建一个binding，在该实例上调用setProperty

binding1 = new Binding()
binding1.setProperty('name','jordan')
shell1 = new GroovyShell(binding1)
shell1.evaluate(new File('script2a.groovy'))

binding2 = new Binding()
binding2.setProperty('name','kobe')




