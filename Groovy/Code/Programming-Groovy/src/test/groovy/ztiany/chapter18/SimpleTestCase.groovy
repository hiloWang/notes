package ztiany.chapter18

/**
 *
 * <br/>    功能描述：
 * <br/>    Email     : ztiany3@gmail.com
 * @author Ztiany
 * @see
 * @since 1.0
 */
class SimpleTestCase extends GroovyTestCase {

    void testListSize() {
        def lst = [1,3]
        assertEquals("arrayList size must be 2", 2 , lst.size())
    }

}

/*


cmd运行groovy SimpleTestCase
.
Time: 0.087

OK (1 test)

 */