package me.ztiany.compiler.rewrite;

import com.netflix.rewrite.ast.Tr;
import com.netflix.rewrite.parse.OracleJdkParser;
import com.netflix.rewrite.parse.Parser;
import com.netflix.rewrite.refactor.Refactor;

/**
 * @author Ztiany
 * Email ztiany3@gmail.com
 * Date 2019/4/10 17:53
 */
public class RewriteMain {

    private static Parser parser = new OracleJdkParser(); // pass binary dependencies to this constructor on a real project

    public static void main(String... args) {
        String a = "class A {{ B.foo(0); }}";
        String b = "class B { static void foo(int n) {} }";

        Tr.CompilationUnit cu = parser.parse(a, /* which depends on */ b);
        Refactor refactor = cu.refactor().changeMethodName(cu.findMethodCalls("B foo(int)"), "bar");
        Tr.CompilationUnit fixed = refactor.fix();
        System.out.println(fixed.print());
    }

}
