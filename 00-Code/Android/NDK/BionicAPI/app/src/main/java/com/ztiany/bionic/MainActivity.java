package com.ztiany.bionic;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ztiany.bionic.pthread.PthreadActivity;

public class MainActivity extends AppCompatActivity {

    private JniBridge mBridge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBridge = new JniBridge();
        setContentView(R.layout.activity_main);
    }

    public void testBionicApi(View view) {
        mBridge.testJni();
    }

    public void pthread(View view) {
        startActivity(new Intent(this, PthreadActivity.class));
    }
}
