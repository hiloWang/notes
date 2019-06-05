package com.ztiany.basic.generic.getgeneractype;

import com.sun.istack.internal.Nullable;

abstract class AbsCallback<T> {

    public void onAfter(@Nullable T t) {

    }

    public abstract T parseNetworkResponse(Object response) throws Exception;
}