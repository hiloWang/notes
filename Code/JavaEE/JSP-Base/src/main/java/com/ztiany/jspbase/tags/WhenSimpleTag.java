package com.ztiany.jspbase.tags;

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class WhenSimpleTag extends SimpleTagSupport {

    private boolean test;

    public void setTest(boolean test) {
        this.test = test;
    }

    public void doTag() throws JspException, IOException {
        if (test) {
            getJspBody().invoke(null);
            //该变父标签的标记
            ChooseSimpleTag parent = (ChooseSimpleTag) getParent();
            parent.setFlag(true);
        }
    }

}
