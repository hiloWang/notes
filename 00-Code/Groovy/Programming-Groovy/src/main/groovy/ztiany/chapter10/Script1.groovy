package ztiany.chapter10

println "in script1"
shell = new GroovyShell()
shell.evaluate(new File('script2.groovy'))
//或者直接
evaluate(new File('script2.groovy'))

