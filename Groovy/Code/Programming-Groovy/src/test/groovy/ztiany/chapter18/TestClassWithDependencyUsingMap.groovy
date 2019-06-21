package ztiany.chapter18

/*
Map有建和值，其值是对象，甚至是闭包，所以使用Map也可以用来代理协作对象

 */
class TestClassWithDependencyUsingMap extends GroovyTestCase{

    void testMethodA() {
        def text = ""
        def fileMock = [write: { val -> text = val }]//使用map来模拟依赖的file

        def testObj = new ClassWithDependency()
        testObj.methodA(1, fileMock)
        assertEquals("the value is $val", text)
    }
}
