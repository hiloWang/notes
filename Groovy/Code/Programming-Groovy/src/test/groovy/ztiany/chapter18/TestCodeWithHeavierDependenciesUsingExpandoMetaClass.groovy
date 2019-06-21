package ztiany.chapter18

/**
 * <br/>   Description：
 * <br/>    Email: ztiany3@gmail.com
 *
 * @author Ztiany
 *      Date : 2016-10-22 00:14
 */
class TestCodeWithHeavierDependenciesUsingExpandoMetaClass extends GroovyTestCase{

    void testMyMethod() {
        def result
        def emc = new ExpandoMetaClass(CodeWithHeavierDependencies, true)
        emc.println = {
            text -> result = text
        }
        emc.someAcion ={->25}
        emc.initialize()
        def testObject = new CodeWithHeavierDependencies()
        testObject.metaClass = emc
        testObject.myMethod()
        assertEquals(35,result)

    }
}
