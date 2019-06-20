package com.ztiany.test.activity_animation;

import android.os.Build;

/**
 * Author Ztiany      <br/>
 * Email ztiany3@gmail.com      <br/>
 * Date 2016-03-16 21:32      <br/>
 * Description：App兼容性的处理
 */
public class AndroidVersion {

    private AndroidVersion() {

    }

    /**
     * @param level 要求的版本
     * @return true when the caller API version is > level
     */
    public static boolean moreThanThe(int level) {
        return Build.VERSION.SDK_INT > level;
    }

    /**
     * @param level 要求的版本
     * @return true when the caller API version >= level
     */
    public static boolean greaterThanOrEqualTo(int level) {
        return Build.VERSION.SDK_INT >= level;
    }

    public static boolean at(int level) {
        return Build.VERSION.SDK_INT == level;
    }

}
