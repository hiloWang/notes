package com.ztiany;

import com.ztiany.annotation.Code;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;

@SupportedAnnotationTypes(
        {
                "com.ztiany.annotation.Code"
        }
)
public class CodeProcessor extends AbstractProcessor {

    private Messager mMessager;
    private Filer mFiler;
    private static final String SUFFIX = "Autogenerate";

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        mMessager = processingEnv.getMessager();
        mFiler = processingEnv.getFiler();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {

        System.out.println("annotations = " + annotations);//annotations - 处理请求的注释类型
        //roundEnv - 有关当前和之前的信息环境, getElementsAnnotatedWith获取含有注解的类型
        System.out.println("getElementsAnnotatedWith = " + roundEnv.getElementsAnnotatedWith(Code.class));

        for (Element element : roundEnv.getElementsAnnotatedWith(Code.class)) {

            Code code = element.getAnnotation(Code.class);
            TypeElement clazz = (TypeElement) element.getEnclosingElement();//获取封装它的类型，其上一级别，比如方法的封装类型就是类或者接口
            try {
                generateCode(element, code, clazz);
            } catch (IOException x) {
                processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, x.toString());
                return false;
            }
        }
        return true;
    }


    private void generateCode(Element element, Code ca, TypeElement clazz) throws IOException {

        JavaFileObject f = mFiler.createSourceFile(clazz.getQualifiedName() + SUFFIX);
        mMessager.printMessage(Diagnostic.Kind.NOTE, "Creating " + f.toUri());

        try (Writer w = f.openWriter()) {
            String pack = clazz.getQualifiedName().toString();//获取类的全限定名
            PrintWriter pw = new PrintWriter(w);
            pw.println("package " + pack.substring(0, pack.lastIndexOf('.')) + ";"); //生成包
            pw.println("\npublic class " + clazz.getSimpleName() + "Autogenerate {");//写类
            pw.println("\n    public " + clazz.getSimpleName() + "Autogenerate() {}");//构造方法
            pw.println("    public final void message() {");//生成一个message方法
            pw.println("\n//" + element);
            pw.println("//" + ca);
            pw.println("\n        System.out.println(\"author:" + ca.author() + "\");");
            pw.println("\n        System.out.println(\"date:" + ca.date() + "\");");
            pw.println("    }");
            pw.println("}");
            pw.flush();
        }

    }
}
