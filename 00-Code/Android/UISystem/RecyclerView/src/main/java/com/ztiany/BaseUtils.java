package com.ztiany;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;


public class BaseUtils {

    @SuppressLint("StaticFieldLeak")
    private static Context mApplication;

    public static void init(Context application) {
        if (mApplication == null) {
            mApplication = application;
        }
    }

    public static Context getAppContext() {
        return mApplication;
    }

    public static Resources getResources() {
        return BaseUtils.getAppContext().getResources();
    }

    public static Resources.Theme getTheme() {
        return BaseUtils.getAppContext().getTheme();
    }

    public static AssetManager getAssets() {
        return BaseUtils.getAppContext().getAssets();
    }

    public static Configuration getConfiguration() {
        return BaseUtils.getResources().getConfiguration();
    }

    public static DisplayMetrics getDisplayMetrics() {
        return BaseUtils.getResources().getDisplayMetrics();
    }

}
