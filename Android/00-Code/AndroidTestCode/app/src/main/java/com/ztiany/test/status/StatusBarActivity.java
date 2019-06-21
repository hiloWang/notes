package com.ztiany.test.status;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.ztiany.test.R;


public class StatusBarActivity  extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemBarCompat.setTranslucent(this, true, true);
        setContentView(R.layout.activity_status);

        findViewById(R.id.status_layout).setFitsSystemWindows(true);

    }
}
