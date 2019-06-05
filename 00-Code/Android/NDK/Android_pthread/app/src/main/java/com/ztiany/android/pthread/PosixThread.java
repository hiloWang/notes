package com.ztiany.android.pthread;

public class PosixThread {

    public native void pthread();

    public native void init();

    public native void destroy();

    static {
        System.loadLibrary("native-lib");
    }
}
