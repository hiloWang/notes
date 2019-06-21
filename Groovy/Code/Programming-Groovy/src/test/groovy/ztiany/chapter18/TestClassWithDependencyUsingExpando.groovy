package ztiany.chapter18

/**
 * <br/>   Description：
 * <br/>    Email: ztiany3@gmail.com
 *
 * @author Ztiany
 *      Date : 2016-10-22 11:14
 */
class TestClassWithDependencyUsingExpando extends GroovyTestCase {

    void testMethodA() {
        def cwd = new ClassWithDependency()

        //使用Expando模式对write方法的调用，并使用text获取其内容
        def methodADelegate = new Expando(text: "", write: { val -> text = val })


        cwd.methodA(1, methodADelegate)
        assertEquals("the value is 1", methodADelegate.text)
    }
}
