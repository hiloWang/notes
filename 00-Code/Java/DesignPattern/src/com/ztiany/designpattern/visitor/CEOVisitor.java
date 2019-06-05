package com.ztiany.designpattern.visitor;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-02-18 19:57
 *         Email: ztiany3@gmail.com
 */
public class CEOVisitor implements Visitor {

    @Override
    public void visit(Engineer engineer) {
        System.out.println("工程师：" + engineer.getName() + ", KPT：" + engineer.getKpi());
    }

    @Override
    public void visit(Manager manager) {
        System.out.println("经理：" + manager.getName() + ", KPT：" + manager.getKpi() + " , 产品数：" + manager.getProducts());
    }
}
