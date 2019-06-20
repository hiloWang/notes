package com.ztiany.jspbase.tags;

import java.io.IOException;
import java.io.StringWriter;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class HtmlFilterSimpleTag extends SimpleTagSupport {

    public void doTag() throws JspException, IOException {
        StringWriter sw = new StringWriter();
        getJspBody().invoke(sw);
        String content = sw.toString();

        content = filter(content);
        PageContext pc = (PageContext) getJspContext();
        pc.getOut().write(content);

    }

    //  >  &gt;  <   &lt;   &   "   '
    private String filter(String message) {
        if (message == null)
            return (null);

        char content[] = new char[message.length()];
        message.getChars(0, message.length(), content, 0);
        StringBuilder result = new StringBuilder(content.length + 50);

        for (char aContent : content) {
            switch (aContent) {
                case '<':
                    result.append("&lt;");
                    break;
                case '>':
                    result.append("&gt;");
                    break;
                case '&':
                    result.append("&amp;");
                    break;
                case '"':
                    result.append("&quot;");
                    break;
                default:
                    result.append(aContent);
            }
        }
        return (result.toString());
    }

}
