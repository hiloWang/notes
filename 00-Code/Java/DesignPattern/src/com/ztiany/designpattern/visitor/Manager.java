package com.ztiany.designpattern.visitor;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-02-18 19:53
 *         Email: ztiany3@gmail.com
 */
public class Manager extends Staff {

    private int products;//产品数量

    public Manager(String name) {
        super(name);
        products = RANDOM.nextInt(10);
    }


    public int getProducts() {
        return products;
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
