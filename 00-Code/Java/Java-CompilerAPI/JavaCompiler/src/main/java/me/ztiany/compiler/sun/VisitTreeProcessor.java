package me.ztiany.compiler.sun;

import com.sun.source.tree.ClassTree;
import com.sun.source.tree.MethodTree;
import com.sun.source.tree.Tree;
import com.sun.source.tree.VariableTree;
import com.sun.source.util.TreePath;
import com.sun.source.util.TreePathScanner;
import com.sun.source.util.Trees;

import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

/* com.sun.source.* 包下暴露的 API 对语法树只能做只读操作，这里打印抽象语法树中的各个节点 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("*")
public class VisitTreeProcessor extends AbstractProcessor {

    private Trees trees;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.trees = Trees.instance(processingEnv);
    }

    public boolean process(Set<? extends TypeElement> types, RoundEnvironment environment) {
        Scanner scanner = new Scanner();
        if (!environment.processingOver()) {
            for (Element element : environment.getRootElements()) {
                TreePath path = trees.getPath(element);
                scanner.scan(path, null);
            }
        }
        return true;
    }

    public class Scanner extends TreePathScanner<Tree, Void> {

        public Tree visitClass(ClassTree node, Void p) {
            System.out.println("类 " + node.getKind() + ": " + node.getSimpleName());
            return super.visitClass(node, p);
        }

        public Tree visitMethod(MethodTree node, Void p) {
            System.out.println("方法 " + node.getKind() + ": " + node.getName());
            return super.visitMethod(node, p);
        }

        public Tree visitVariable(VariableTree node, Void p) {
            if (this.getCurrentPath().getParentPath().getLeaf() instanceof ClassTree) {
                System.out.println("字段 " + node.getKind() + ": " + node.getName());
            }
            return super.visitVariable(node, p);
        }
    }

}