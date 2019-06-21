package com.ztiany.basic.generic.study;


public class Pair<T> {

    private T mFirst;
    private T mSecond;

    void setFirst(T t) {
        mFirst = t;
    }
    public T getFirst() {
        return mFirst;
    }

    public void setSecond(T second) {
        mSecond = second;
    }

    public T getSecond() {
        return mSecond;
    }
}
