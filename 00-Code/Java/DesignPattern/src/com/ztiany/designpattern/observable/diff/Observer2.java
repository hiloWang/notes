package com.ztiany.designpattern.observable.diff;

import com.ztiany.designpattern.observable.pull.Subject;

/**
 * 区别对待观察者
 */
public interface Observer2 {
    void update(Subject subject);
    int[] getCareType();
}
