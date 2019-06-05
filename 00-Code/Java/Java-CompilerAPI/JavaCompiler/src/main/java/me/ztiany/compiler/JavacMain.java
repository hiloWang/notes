package me.ztiany.compiler;


import com.sun.tools.javac.main.Main;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.processing.Processor;
import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import me.ztiany.compiler.jsr269.ElementVisitProcessor;
import me.ztiany.compiler.sun.AssertToIfThrowProcessor;
import me.ztiany.compiler.sun.ClearLogProcessor;
import me.ztiany.compiler.sun.VisitAllNodeProcessor;
import me.ztiany.compiler.sun.VisitTreeProcessor;

import static me.ztiany.compiler.Utils.close;

/**
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
public class JavacMain {

    private static final String APP_SOURCE_DIR = "JavaCompiler/src/main/java/me/ztiany/compiler/App.java";
    private static final String TARGET_OPTION = "-d";
    private static final String TARGET_DIR = "JavaCompiler/build/manual/";

    public static void main(String... args) {
        //javaToolsInternalApi();
        //jsr199_1();
        //jsr199_2();
        //jsr269();
        //jsr269TreeScanner();
        //javacInternalAPI();
        //assertToIfThrow();
        clearLog();
    }


    /*
     * 在没有引入 JSR-199 前，只能使用 javac 源码提供内部 API，这部分API不是标准JAVA的一部分：官方的警告标注：
     *      这不是任何支持的API的一部分。如果您编写依赖于此的代码，则需要您自担风险。此代码及其内部接口如有更改或删除，恕不另行通知。
     */
    private static void javaToolsInternalApi() {
        Main compiler = new Main("javac");
        System.out.println(new File(".").getAbsolutePath());
        new File(TARGET_DIR).mkdirs();
        compiler.compile(new String[]{APP_SOURCE_DIR, TARGET_OPTION, TARGET_DIR});
    }

    /* JSR-199（Java Compiler API） 引入了 Java 编译器 API，`javax.tools.JavaCompiler` 就是其中之一 */
    private static void jsr199_1() {
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        int run = javaCompiler.run(null, null, null, APP_SOURCE_DIR, "-d", "JavaCompiler/build/manual");
        System.out.println(run);
    }

    private static void jsr199_2() {
        doCompile(Collections.singletonList(new File(APP_SOURCE_DIR)), Arrays.asList(TARGET_OPTION, TARGET_DIR), null);
    }

    private static void doCompile(List<File> source, List<String> args, List<Processor> processors) {
        //编译器
        JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        //编译过程中诊断信息收集器
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        //标准Java文件管理器
        StandardJavaFileManager standardFileManager = javaCompiler.getStandardFileManager(diagnostics, null, null);

        //构建编译任务
        Iterable<? extends JavaFileObject> compilationUnits = standardFileManager.getJavaFileObjectsFromFiles(source);
        JavaCompiler.CompilationTask task = javaCompiler.getTask(null, standardFileManager, diagnostics, args, null, compilationUnits);

        //设置编译处理器
        if (processors != null) {
            task.setProcessors(processors);
        }

        //执行编译任务
        task.call();

        //打印编译过程中的诊断信息
        System.out.println();
        System.out.println("--------------------------------------------------------------------------------");
        for (Diagnostic<? extends JavaFileObject> diagnostic : diagnostics.getDiagnostics()) {
            System.out.format("Error on line %d in %s\n%s\n", diagnostic.getLineNumber(), diagnostic.getSource(), diagnostic.getMessage(null));
        }

        //关闭资源
        close(standardFileManager);
    }


    /*
     * JSR-269（Pluggable Annotation Processing API）引入了编译器注解处理：
     *      整个类文件被扫描，包括内部类以及全部方法、构造方法和字段。注解处理在填充符号表之后进行。
     *      ElementScanner 类扫描的 Element 其实就是符号 Symbol：public abstract class Symbol extends AnnoConstruct implements Element
     */
    private static void jsr269() {
        doCompile(Collections.singletonList(new File(APP_SOURCE_DIR)), Arrays.asList(TARGET_OPTION, TARGET_DIR), Collections.singletonList(new ElementVisitProcessor()));
    }

    /*
     * com.sun.tools.javac.* 内部 API 和编译器的实现类库中，提供了编译器操作 AST 的功能，从而可以在源码编译器修改被编译的源码：
     *
     *      填充符号表前一步是构造语法树。对语法树的扫描，com.sun.source.* 提供了扫描器 TreeScanner。
     *      获取语法树是通过工具类 Trees 的 getTree 方法完成的。 com.sun.source.* 包下暴露的 API 对语法树只能做只读操作，
     *      功能有限，要想修改语法树必须使用 javac 的内部 API。
     */
    private static void jsr269TreeScanner() {
        doCompile(Collections.singletonList(new File(APP_SOURCE_DIR)), Arrays.asList(TARGET_OPTION, TARGET_DIR), Collections.singletonList(new VisitTreeProcessor()));
    }


    /* 内部API，访问所有 AST 节点   */
    private static void javacInternalAPI() {
        doCompile(Collections.singletonList(new File(APP_SOURCE_DIR)), Arrays.asList(TARGET_OPTION, TARGET_DIR), Collections.singletonList(new VisitAllNodeProcessor()));
    }

    /* 示例：断言修改为 if throw */
    private static void assertToIfThrow() {
        doCompile(Collections.singletonList(new File(APP_SOURCE_DIR)), Arrays.asList(TARGET_OPTION, TARGET_DIR), Collections.singletonList(new AssertToIfThrowProcessor()));
    }

    private static void clearLog() {
        doCompile(Collections.singletonList(new File(APP_SOURCE_DIR)), Arrays.asList(TARGET_OPTION, TARGET_DIR), Collections.singletonList(new ClearLogProcessor()));
    }

}
