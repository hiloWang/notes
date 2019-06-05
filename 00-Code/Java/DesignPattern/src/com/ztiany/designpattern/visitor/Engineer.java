package com.ztiany.designpattern.visitor;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Ztiany
 *         Date : 2017-02-18 19:55
 *         Email: ztiany3@gmail.com
 */
public class Engineer extends Staff {

    private long codeLines;

    public Engineer(String name) {
        super(name);
        codeLines = RANDOM.nextInt(10 * 10000);
    }

    public long getCodeLines() {
        return codeLines;
    }


    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }
}
