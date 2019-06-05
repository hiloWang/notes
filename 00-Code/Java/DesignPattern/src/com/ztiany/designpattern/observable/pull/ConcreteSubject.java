package com.ztiany.designpattern.observable.pull;

/**
 * <br/>    功能描述：
 * <br/>    Email     : ztiany3@gmail.com
 *
 * @author Ztiany
 * @see
 * @since 1.0
 */
public class ConcreteSubject extends Subject {

    private String mContent;

    public void setContent(String content) {
        mContent = content;
        notifyObservers();
    }

    public String getContent() {
        return mContent;
    }
}
