package com.ztiany.jni.sample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    static {
        System.loadLibrary("native-lib");
    }

    private JniBridge mJniBridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mJniBridge = new JniBridge();
    }


    //返回字符串
    public void stringFromC(View view) {
        String stringFromC = JniBridge.stringFromC();
        AppContext.showToast(stringFromC);
    }

    //模拟登录
    public void intFromC(View view) {
        int a = 10;
        int b = 20;
        AppContext.showToast("intFromC: " + (mJniBridge.intFromC(a, b)));
    }

    //修改每个元素后返回
    public void addArray(View view) {
        int[] arr = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        mJniBridge.addArray(arr, 100);
        Log.d(TAG, "addArray: " + Arrays.toString(arr));
    }

    //c语言的冒泡排序
    public void bubbleSort(View view) {
        int[] originArr = initIntArr();
        Log.d(TAG, "originArr: " + Arrays.toString(originArr));
        long start = System.currentTimeMillis();
        mJniBridge.bubbleSort(originArr);
        Log.d(TAG, "originArr: " + Arrays.toString(originArr));
        Log.d(TAG, "bubbleSort use time: " + (System.currentTimeMillis() - start));
    }

    //加密
    public void encryption(View view) {
        String password = "Java password";
        String encryption = mJniBridge.encryption(password);
        Log.d(TAG, "加密 " + encryption);
    }

    //让C调用Java
    public void callJava(View view) {
        mJniBridge.callJava("Java message");
    }

    //让C抛出异常
    public void throwError(View view) {
        try {
            mJniBridge.throwError("抛出一个异常吧");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //调用C动态方法注册
    public void dynamicRegisterFromJni(View view) {
        String registerFromJni = mJniBridge.dynamicRegisterFromJni();
        AppContext.showToast(registerFromJni);
    }


    private int[] initIntArr() {
        int[] arr = new int[10000];
        for (int x = 0; x < arr.length; x++) {
            arr[x] = (int) (Math.random() * 10000 + 1);
        }
        return arr;
    }

}
