package com.ztiany;

import com.ztiany.annotation.SamplePrint;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 * APT API 测试
 * <pre>
 *  方法的调用顺序
 *          init
 *          getSupportedSourceVersion
 *          getSupportedAnnotationTypes
 *          process
 * </pre>
 *
 * @author Ztiany
 * Date : 2017-02-15 22:33
 * Email: ztiany3@gmail.com
 */
public class SamplePrintProcessor extends AbstractProcessor {

    private Messager mMessager;

    /**
     * 该方法有注解处理器自动调用，其中ProcessingEnvironment类提供了很多有用的工具类：Filter，Types，Elements，Messager等
     */
    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        Elements elementUtils = processingEnv.getElementUtils();//Elements中包含用于操作Element的工具方法
        Filer filer = processingEnv.getFiler();//Filter用来创建新的源文件，class文件以及辅助文件
        Locale locale = processingEnv.getLocale();
        //Messager用来报告错误，警告和其他提示信息
        mMessager = processingEnv.getMessager();
        Map<String, String> options = processingEnv.getOptions();
        SourceVersion sourceVersion = processingEnv.getSourceVersion();
        Types typeUtils = processingEnv.getTypeUtils();//Types中包含用于操作TypeMirror的工具方法
        super.init(processingEnv);
    }

    /**
     * 该方法返回字符串的集合表示该处理器用于处理那些注解
     */
    @Override
    public Set<String> getSupportedAnnotationTypes() {
        LinkedHashSet<String> annotations = new LinkedHashSet<>();
        annotations.add(SamplePrint.class.getCanonicalName());
        return annotations;
    }

    /**
     * 该方法用来指定支持的java版本，一般来说我们都是支持到最新版本，因此直接返回SourceVersion.latestSupported(）即可
     */
    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }


    /**
     * 该方法是注解处理器处理注解的主要地方，我们需要在这里写扫描和处理注解的代码，
     * 以及最终生成的java文件。其中需要深入的是RoundEnvironment类，该用于查找出程序元素上使用的注解
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        annotations.forEach((Consumer<TypeElement>) typeElement -> {
            for (Element element : roundEnv.getElementsAnnotatedWith(typeElement)) {
                mMessager.printMessage(Diagnostic.Kind.NOTE, element.toString());
            }
        });

        return true;
    }
}
