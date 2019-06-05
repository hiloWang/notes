package ztiany.chapter18
/**
 * <br/>   Description：
 * <br/>    Email: ztiany3@gmail.com
 *
 * @author Ztiany
 *      Date : 2016-10-22 00:09
 */
class TestCodeWithHeavierDependenciesUsingCategory extends GroovyTestCase{


    void testMyMethod() {
        def testObj = new CodeWithHeavierDependencies()
            use(MockHelper){
                testObj.myMethod()
                assertEquals 34, MockHelper.result
            }

    }


    class MockHelper{
        def static result

        def static println(self, text) {
            result = text
        }

        def static someAction(CodeWithHeavierDependencies self) {
                24
        }
    }

}
