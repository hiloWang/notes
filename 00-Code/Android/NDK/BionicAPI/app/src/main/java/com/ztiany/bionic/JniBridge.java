package com.ztiany.bionic;

import android.util.Log;

import static com.ztiany.bionic.Constant.TAG;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2018-03-19 22:50
 */
public class JniBridge {

    public native void testJni();

    ///////////////////////////////////////////////////////////////////////////
    // naive thread
    ///////////////////////////////////////////////////////////////////////////

    @SuppressWarnings("unused")
    //call by native
    private void onNativeMessage(String message) {
        Log.d(TAG, message);
    }

    public native void nativeInit();

    public native void nativeFree();

    public native void posixThreads(int threads, int iterations);
}
