package com.ztiany.bionic;

import android.app.Application;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2018-03-19 22:49
 */
public class AppContext extends Application {

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
