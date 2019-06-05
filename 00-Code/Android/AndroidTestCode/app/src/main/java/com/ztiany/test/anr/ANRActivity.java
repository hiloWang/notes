package com.ztiany.test.anr;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ztiany.test.R;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2018-02-01 22:11
 */
public class ANRActivity extends AppCompatActivity {

    private static final String TAG = ANRActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anr);
    }

    public void makeSimpleANR(View view) {
        SystemClock.sleep(6000);
    }

    public void makeComplexANR(View view) {
        new Thread() {
            @Override
            public void run() {
                threadANR();
            }
        }.start();
        SystemClock.sleep(20);
        initView();
    }

    private synchronized void initView() {
        Log.d(TAG, "initView() called");
    }

    private synchronized void threadANR() {
        SystemClock.sleep(26000);
    }
}
