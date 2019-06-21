package com.ztiany.struts2.mock;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 对应框架配置文件中的action元素
 */
public class Action implements Serializable {

    private String name;
    private String className;
    private String method = "execute";
    private List<Result> results = new ArrayList<Result>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Action [className=" + className + ", method=" + method
                + ", name=" + name + ", results=" + results + "]";
    }

}
