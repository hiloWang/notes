package me.ztiany.compiler.sun;

import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.Names;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;

@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("*")
@SuppressWarnings("all")
public class VisitAllNodeProcessor extends AbstractProcessor {

    private JavacProcessingEnvironment env;
    private Trees trees;
    private TreeMaker maker;
    private Names names;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        this.trees = Trees.instance(processingEnv);
        //把处理环境（ProcessingEnvironment）强制转换成了编译器的内部类型。
        this.env = (JavacProcessingEnvironment) processingEnv;
        this.maker = TreeMaker.instance(env.getContext());
        this.names = Names.instance(env.getContext());
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return true;
        }

        Set<? extends Element> elements = roundEnv.getRootElements();

        for (Element element : elements) {
            if (element.getKind() == ElementKind.CLASS) {

                JCTree tree = (JCTree) trees.getTree(element);
                TreeTranslator visitor = new AssertTranslator();
                tree.accept(visitor);
            }
        }

        return true;
    }


    private class AssertTranslator extends TreeTranslator {

        @Override
        public void visitTopLevel(JCTree.JCCompilationUnit tree) {
            super.visitTopLevel(tree);
            System.out.println("visitTopLevel：" + tree);
        }

        @Override
        public void visitImport(JCTree.JCImport tree) {
            super.visitImport(tree);
            System.out.println("visitImport：" + tree);
        }

        @Override
        public void visitClassDef(JCTree.JCClassDecl tree) {
            super.visitClassDef(tree);
            System.out.println("visitClassDef：" + tree);
        }

        @Override
        public void visitMethodDef(JCTree.JCMethodDecl tree) {
            super.visitMethodDef(tree);
            System.out.println("visitMethodDef：" + tree);
        }

        @Override
        public void visitAssert(JCTree.JCAssert tree) {
            super.visitAssert(tree);
            System.out.println("visitAssert：" + tree);
        }

    }

}
