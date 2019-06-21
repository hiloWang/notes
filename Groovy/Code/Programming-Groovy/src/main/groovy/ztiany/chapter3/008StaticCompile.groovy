package ztiany.chapter3

import groovy.transform.CompileStatic;

/*
Groovy的动态类型和元编程的优点显而易见，但是这些都要以性能作为代价，性能的下降与代码、所调试的方法的个数等因素有关，当不需要
元编程和动态能力时，与等价的Java代码相比，性能损失了10%，我们可以关闭动态类型，阻止元编程，放弃多方法，并让Groovy生成性能足以和
Java媲美的高效字节码，使用@CompileStatic注解可以执行静态编译，这样目标代码生成的字节码回合javac生成的字节码很像
 */

def shot(String str) {
    println str.toUpperCase()
}

@CompileStatic
def shot1(String str) {
    println str.toUpperCase()
}

