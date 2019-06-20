package com.ztiany.mediaplayer.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ztiany.mediaplayer.android.test.NormalActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void normalPlayer(View view) {
        startActivity(new Intent(this, NormalActivity.class));
    }

}
