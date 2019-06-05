package me.ztiany.ast.processor;

import com.google.auto.service.AutoService;
import com.sun.source.util.Trees;
import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.processing.JavacProcessingEnvironment;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import com.sun.tools.javac.util.Name;
import com.sun.tools.javac.util.Names;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import java.util.Set;

/**
 * @author yulewei on 17/4/18.
 */
@AutoService(Processor.class)
@SupportedAnnotationTypes("*")
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class DataAnnotationProcessor extends AbstractProcessor {

    private JavacProcessingEnvironment env;
    private Trees trees;
    private TreeMaker treeMaker;
    private Names names;

    @Override
    public void init(ProcessingEnvironment processingEnv) {
        this.trees = Trees.instance(processingEnv);
        this.env = (JavacProcessingEnvironment) processingEnv;
        this.treeMaker = TreeMaker.instance(env.getContext());
        this.names = Names.instance(env.getContext());
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if (roundEnv.processingOver()) {
            return true;
        }

        //遍历所有标注了 Data.class 元素
        for (Element element : roundEnv.getElementsAnnotatedWith(Data.class)) {

            System.out.println("element-------------" + element);

            //如果不是 Type(类、接口...)
            if (!(element instanceof TypeElement)) {
                continue;
            }

            TypeElement classElement = (TypeElement) element;
            //元素 --> 节点
            JCTree.JCClassDecl classDecl = (JCTree.JCClassDecl) trees.getTree(classElement);

            List<JCTree> methodDecls = List.nil();
            for (JCTree tree : classDecl.defs) {

                if (tree instanceof JCTree.JCVariableDecl) {
                    //创建一个方法节点，这是是字段的getter
                    JCTree.JCMethodDecl methodGetter = this.createGetter((JCTree.JCVariableDecl) tree);
                    //创建一个方法节点，这是是字段的setter
                    JCTree.JCMethodDecl methodSetter = this.createSetter((JCTree.JCVariableDecl) tree);
                    //添加到方法声明列表中
                    methodDecls = methodDecls.append(methodGetter);
                    methodDecls = methodDecls.append(methodSetter);
                }
            }

            classDecl.defs = classDecl.defs.appendList(methodDecls);
        }

        return true;
    }

    private JCTree.JCMethodDecl createGetter(JCTree.JCVariableDecl field) {
        System.out.println("DataAnnotationProcessor.createGetter "+field);
        //return 语句
        JCTree.JCStatement returnStatement = treeMaker.Return(treeMaker.Ident(field));
        //方法体
        JCTree.JCBlock methodBody = treeMaker.Block(0, List.of(returnStatement));
        //方法名
        Name methodName = names.fromString("get" + this.toTitleCase(field.getName().toString()));
        //方法类型
        JCTree.JCExpression methodType = (JCTree.JCExpression) field.getType();

        return treeMaker.MethodDef(//创建一个方法声明
                treeMaker.Modifiers(Flags.PUBLIC),//public的方法
                methodName, //方法名
                methodType,//方法返回值
                List.nil(),//类型参数
                List.nil(),//参数
                List.nil(),//异常声明
                methodBody,//方法体
                null);//默认值
    }

    private JCTree.JCMethodDecl createSetter(JCTree.JCVariableDecl field) {
        System.out.println("DataAnnotationProcessor.createSetter "+field);
        //字段访问节点
        JCTree.JCFieldAccess thisX = treeMaker.Select(treeMaker.Ident(names.fromString("this")), field.name);

        //赋值节点
        JCTree.JCAssign assign = treeMaker.Assign(thisX, treeMaker.Ident(field.name));

        //方法体
        JCTree.JCBlock methodBody = treeMaker.Block(0, List.of(treeMaker.Exec(assign)));

        //方法名
        Name methodName = names.fromString("set" + this.toTitleCase(field.getName().toString()));

        //方法参数
        JCTree.JCVariableDecl param = treeMaker.VarDef(treeMaker.Modifiers(Flags.PARAMETER), field.name, field.vartype, null);

        //方法返回值
        JCTree.JCExpression methodType = treeMaker.Type(new Type.JCVoidType());

        return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC),
                methodName,
                methodType,
                List.nil(),
                List.of(param),
                List.nil(),
                methodBody,
                null);
    }

    /**
     * 首字母大写
     */
    private String toTitleCase(String str) {
        char first = str.charAt(0);
        if (first >= 'a' && first <= 'z') {
            first -= 32;
        }
        return first + str.substring(1);
    }

} 