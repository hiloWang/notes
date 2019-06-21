package com.ztiany.android.pthread;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private PosixThread p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        p = new PosixThread();
        p.init();
    }

    public void test(View btn) {
        p.pthread();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        p.destroy();
    }

}
