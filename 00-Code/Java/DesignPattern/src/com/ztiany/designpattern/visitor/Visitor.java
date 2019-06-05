package com.ztiany.designpattern.visitor;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-02-18 19:52
 *         Email: ztiany3@gmail.com
 */
public interface Visitor  {

    void visit(Engineer engineer);

    void visit(Manager manager);
}
