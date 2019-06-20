package com.ztiany.designpattern.visitor;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-02-18 19:58
 *         Email: ztiany3@gmail.com
 */
public class CTOVisitor implements Visitor {

    @Override
    public void visit(Engineer engineer) {
        System.out.println("工程师：" + engineer.getName() + ", 代码数：" + engineer.getCodeLines());
    }

    @Override
    public void visit(Manager manager) {
        System.out.println("经理：" + manager.getName() + " , 产品数：" + manager.getProducts());
    }
}
