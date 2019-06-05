package ztiany.chapter18

/*
如果简单的为myMethod写一个单元测试，它会很慢，而且无法对myMethod调用的结果进行断言，因为其什么都没有返回，所以
需要捕获其打印的值，并施以断言。
 */
class CodeWithHeavierDependencies {
    public void myMethod() {
        def value = sameAcion() +10
        println(value)
    }

    int sameAction() {
        Thread.sleep(5000)
        Math.random() * 100
    }
}
