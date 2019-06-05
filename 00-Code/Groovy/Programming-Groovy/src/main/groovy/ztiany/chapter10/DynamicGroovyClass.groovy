package ztiany.chapter10

/**
 *
 * <br/>    功能描述：
 * <br/>    Email     : ztiany3@gmail.com
 * @author Ztiany
 * @see
 * @since 1.0
 */
class DynamicGroovyClass {
    def methodMissing(String name, args) {
        println "you call $name with ${args}"
        args.size()
    }

}
