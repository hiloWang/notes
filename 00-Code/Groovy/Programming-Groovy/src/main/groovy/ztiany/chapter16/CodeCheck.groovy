package ztiany.chapter16

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.ConstructorNode
import org.codehaus.groovy.ast.FieldNode
import org.codehaus.groovy.ast.GroovyClassVisitor
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.PropertyNode
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.syntax.SyntaxException
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation

/*
 Groovy的编译阶段分为：初始化、解析、转换、语义分析、规范化、指令选择、class生成、输出、结束。

 首个合理的时机是在语义分析阶段之后，AST会此时被创建出来
  */

@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)//声明在语义分析后执行
class CodeCheck implements ASTTransformation {

    @Override
    void visit(ASTNode[] nodes, SourceUnit source) {
        source.getAST().classes.each {
            classNode ->
                classNode.visitContents(new OurClassVisitor(source));
        }
    }

    class OurClassVisitor implements GroovyClassVisitor{
        SourceUnit mSourceUnit;
        OurClassVisitor(SourceUnit sourceUnitClass) {
            mSourceUnit = sourceUnitClass
        }

        @Override
        void visitClass(ClassNode node) {

        }

        @Override
        void visitConstructor(ConstructorNode node) {

        }

        def reportError(message, lineNumber, columnNumber) {
            mSourceUnit.addError(new SyntaxException(message,lineNumber,columnNumber))
        }

        @Override
        void visitMethod(MethodNode node) {
            if (node.name.size() == 1) {
                reportError "Make method name descriptive,avoid single letter names",node.lineNumber, node.columnNumber
            }
            node.parameters.each {
                parameter ->
                    if (parameter.name.size() == 1) {
                        reportError "Single letter parameters are morally wrong!",parameter.lineNumber,parameter.columnNumber
                    }
            }
        }

        @Override
        void visitField(FieldNode node) {

        }

        @Override
        void visitProperty(PropertyNode node) {

        }
    }
}

/*

准备好org.codehaus.groovy.transform.ASTTransformation文件，将其放在services中，其中内容为:ztiany.chapter16.CodeCheck

groovyc -d classes ztiany\chapter16\CodeCheck.groovy        把CodeCheck编译在classes文件夹中
jar -cf CodeCheck.jar -C classes ztiany -C manifest .    或者 jar -cf ca.jar -C manifest/ . -C classes/ .
groovyc -classpath CodeCheck.jar ztiany\chapter16\Smelly.groovy

*/