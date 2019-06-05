package com.ztiany.base;

import android.widget.Toast;

import com.ztiany.base.presentation.ErrorHandler;

import javax.inject.Inject;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-05-22 11:20
 */
class AppErrorHandler implements ErrorHandler {

    @Inject
    AppErrorHandler() {

    }

    @Override
    public void handleError(Throwable throwable) {
        Toast.makeText(AppContext.getContext(), "Debug：发生错误了 ： " + throwable, Toast.LENGTH_SHORT).show();
    }
}
