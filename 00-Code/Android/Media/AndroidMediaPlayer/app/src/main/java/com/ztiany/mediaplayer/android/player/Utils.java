package com.ztiany.mediaplayer.android.player;

import android.util.Log;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-16 16:45
 */
class Utils {

    private static final String TAG = AndroidMediaPlayer.class.getSimpleName();

    static int d(String msg) {
        return Log.d(TAG, msg);
    }

    static int e(String msg) {
        return Log.e(TAG, msg);
    }
}
