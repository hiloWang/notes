package com.ztiany.appsecurity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, getSignature(this));
        System.loadLibrary("app-security");
    }


    /**
     * 展示了如何用Java代码获取签名
     */
    public static String getSignature(Context context) {
        try {
            // 下面几行代码展示如何任意获取Context对象，在jni中也可以使用这种方式
            //Class<?> activityThreadClz = Class.forName("android.app.ActivityThread");
            //Method currentApplication = activityThreadClz.getMethod("currentApplication");
            //Application application = (Application) currentApplication.invoke(null);
            //PackageManager pm = application.getPackageManager();
            //PackageInfo pi = pm.getPackageInfo(application.getPackageName(), PackageManager.GET_SIGNATURES);
            PackageManager pm = context.getPackageManager();
            @SuppressLint("PackageManagerGetSignatures")
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signatures = pi.signatures;
            Signature signature0 = signatures[0];
            return signature0.toCharsString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}
