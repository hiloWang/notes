package me.ztiany.compiler.sun;

import com.sun.source.util.Trees;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.tree.TreeTranslator;
import com.sun.tools.javac.util.List;
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
import javax.tools.Diagnostic;

/**
 * 把源码中的 assert 替换为 if throw
 *
 * @author ztiany
 * Email: ztiany3@gmail.com
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("*")
@SuppressWarnings("all")
public class AssertToIfThrowProcessor extends AbstractProcessor {

    //Java编译器处理环境
    private JavacProcessingEnvironment env;
    //Trees – JSR269的一个工具类，用于联系程序元素和树节点。比如，对于一个方法元素，我们可以获得这个元素对应的AST树节点。
    private Trees trees;
    //TreeMaker – 编译器的内部组件，是用于创建树节点的工厂类。工厂类里面方法的命名方式跟Javac源代码里面的方法是统一的。
    private TreeMaker maker;
    //Name – 另一个编译器的内部组件。Name类是编译器内部字符串的一个抽象。为了提高效率，Javac使用了哈希字符串。
    private Names names;
    //记录替换的次数
    private int tally;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        this.trees = Trees.instance(processingEnv);
        //把处理环境（ProcessingEnvironment）强制转换成了编译器的内部类型。
        this.env = (JavacProcessingEnvironment) processingEnv;
        this.maker = TreeMaker.instance(env.getContext());
        this.names = Names.instance(env.getContext());
        tally = 0;
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        //如果这一轮生成的类型不会接受后续的注释中加工则返回true
        if (roundEnv.processingOver()) {
            //用于报告处理过的assertion语句数量。这个语句只有在最后一轮处理才会执行。
            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, tally + " assertions inlined.");
            return true;
        }

        //获取所有的根元素
        Set<? extends Element> elements = roundEnv.getRootElements();

        //遍历所有的程序元素，为每一个类都重写AST。
        for (Element element : elements) {

            if (element.getKind() == ElementKind.CLASS) {
                //把JSR269的树节点转换成编译器内部的树节点。这两种树节点的不同之处在于：
                //      JSR269节点是停留在方法层的（即方法method是最基本的元素，不会再细分下去）
                //      内部的AST节点，是所有元素（包括方法以下的）都可以访问的
                //
                // 我们要访问每一个语句，所以需要访问到AST的所有节点。
                JCTree tree = (JCTree) trees.getTree(element);

                TreeTranslator visitor = new AssertTranslator();

                tree.accept(visitor);
            }
        }

        return true;
    }


    /*
    树的转换是通过继承 TreeTranslator 来完成的，TreeTranslator 本身是继承自 TreeVisitor 的。这些类都不是JSR269的一部分。所以从这里开始，我们所写的所有代码都是在编译器内部工作的。

    这个类实现了 AST 重写。AssertTranslator 继承了TreeTranslator，TreeTranslator本身是不会转换任何节点的。为了转换 assertion 语句，我们需要覆盖默认的 TreeTranslator.visitAssert 方法，
    正在转换的节点会被当做参数传入到方法中。我们做的转换的结果通过赋值给变量 TreeTranslator.result 而返回，按照惯例，一个转换方法应该这样生成：

        1. 调用父类的转换方法，以确保转换可以被应用到。
        2. 执行真正的转换
        3. 把转换结果赋值给TreeTranslator.result。结果的类型不一定要和传进来的参数的类型一样。相反，只要java编译器允许，
            我们可以返回任何类型的节点。这里TreeTranslator本身没有限制类型，但是如果返回了错误的类型，
            那么就很有在后续过程中产生灾难性后果。
     */
    private class AssertTranslator extends TreeTranslator {

        @Override
        public void visitAssert(JCTree.JCAssert tree) {
            super.visitAssert(tree);
            //result 是 TreeTranslator 的成员变量，修改result，则原节点将被替换
            result = makeIfThrowException(tree);
            tally++;
        }


        private JCTree.JCStatement makeIfThrowException(JCTree.JCAssert node) {
            // 创建节点来构造 if (!(condition) throw new AssertionError(detail); 语句
            List<JCTree.JCExpression> args = node.getDetail() == null ? List.nil() : List.of(node.detail);

            //这是一个 new AssertionError() 表达式
            JCTree.JCExpression expr = maker.NewClass(
                    null,
                    null,
                    maker.Ident(names.fromString("AssertionError")),
                    args,
                    null);

            //这是一个 if(condition){...}else 语句
            return maker.If(
                    maker.Unary(JCTree.Tag.NOT, node.cond),//if的条件
                    maker.Throw(expr), //条件满足时执行的语句，这里抛出一个异常
                    null);//条件不满足时执行的语句
        }
    }

}
