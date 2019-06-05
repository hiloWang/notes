package com.ztiany.bionic.pthread;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ztiany.bionic.JniBridge;
import com.ztiany.bionic.R;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2018-03-19 23:58
 */
public class PthreadActivity extends AppCompatActivity {

    private static final String TAG = "PthreadActivity";

    private JniBridge mJniBridge;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mJniBridge = new JniBridge();
        setContentView(R.layout.activity_pthread);
    }


    public void initNative(View view) {
        mJniBridge.nativeInit();
    }

    public void startNative(View view) {
        mJniBridge.posixThreads(10, 10);
    }

    @Override
    protected void onDestroy() {
        mJniBridge.nativeFree();
        super.onDestroy();
    }
}
