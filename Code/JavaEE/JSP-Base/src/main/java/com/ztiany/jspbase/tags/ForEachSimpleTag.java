package com.ztiany.jspbase.tags;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class ForEachSimpleTag extends SimpleTagSupport {

    private String var;
    private Collection collection = new ArrayList();

    public void setItems(Object items) {
        //根据items的具体类型，把元素搞到collection中
        if (items instanceof List) {
            collection = (List) items;
        } else if (items instanceof Set) {
            collection = (Set) items;
        } else if (items instanceof Map) {
            collection = ((Map) items).entrySet();
        } else if (items.getClass().isArray()) {
            //是数组：含基本和引用类型的数组
            //数组的长度
            int length = Array.getLength(items);
            for (int i = 0; i < length; i++) {
                collection.add(Array.get(items, i));
            }
        } else {
            throw new RuntimeException("不支持的类型");
        }
    }

    public void setVar(String var) {
        this.var = var;
    }

    public void doTag() throws JspException, IOException {
        PageContext pc = (PageContext) getJspContext();
        for (Object obj : collection) {
            pc.setAttribute(var, obj);
            getJspBody().invoke(null);
        }
        pc.removeAttribute(var);
    }

}
