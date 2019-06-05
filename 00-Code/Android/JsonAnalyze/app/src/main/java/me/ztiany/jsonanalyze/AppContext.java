package me.ztiany.jsonanalyze;

import android.app.Application;

/**
 * @author Ztiany
 * Date : 2018-08-13 11:14
 */
public class AppContext extends Application {

    private static AppContext sAppContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sAppContext = this;
    }

    public static AppContext getAppContext() {
        return sAppContext;
    }

}

