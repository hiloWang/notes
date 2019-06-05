package com.ztiany.designpattern.observable.push;

import java.util.ArrayList;
import java.util.List;

/**
 * <br/>    功能描述：
 * <br/>    Email     : ztiany3@gmail.com
 *
 * @author Ztiany
 * @see
 * @since 1.0
 */
public class Subject {

    private List<Observer> mObservers = new ArrayList<>();

    public void attact(Observer observer) {
        mObservers.add(observer);
    }

    private void detach(Observer observer) {
        mObservers.remove(observer);
    }

    public void notifyObservers(String content) {
        for (Observer observer : mObservers) {
            observer.update(content);
        }
    }
}
