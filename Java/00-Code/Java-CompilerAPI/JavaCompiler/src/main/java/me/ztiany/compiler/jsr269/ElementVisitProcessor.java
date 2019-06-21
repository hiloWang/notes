package me.ztiany.compiler.jsr269;


import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementScanner7;

//指定支持的java版本
@SupportedSourceVersion(SourceVersion.RELEASE_7)
//表示该处理器用于处理哪些注解，这里表示处理所有元素
@SupportedAnnotationTypes("*")
public class ElementVisitProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    /**
     * 该方法是注解处理器处理注解的主要地方，我们需要在这里写扫描和处理注解的代码， 以及最终生成的java文件。其中需要深入的是RoundEnvironment类，该用于查找出程序元素上使用的注解
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Scanner scanner = new Scanner();
        if (!roundEnv.processingOver()) {
            for (Element element : roundEnv.getRootElements()) {
                scanner.scan(element);
            }
        }

        return true;
    }

    public class Scanner extends ElementScanner7<Void, Void> {

        public Void visitType(TypeElement element, Void p) {
            System.out.println("类 " + element.getKind() + ": " + element.getSimpleName());
            return super.visitType(element, p);
        }

        public Void visitExecutable(ExecutableElement element, Void p) {
            System.out.println("方法 " + element.getKind() + ": " + element.getSimpleName());
            return super.visitExecutable(element, p);
        }

        public Void visitVariable(VariableElement element, Void p) {
            if (element.getEnclosingElement().getKind() == ElementKind.CLASS) {
                System.out.println("字段 " + element.getKind() + ": " + element.getSimpleName());
            }
            return super.visitVariable(element, p);
        }

    }

}
