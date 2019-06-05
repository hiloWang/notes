package com.itheima.mobileguard.activities;

import android.os.Bundle;

import com.itheima.mobileguard.R;

public class LostSetup1Activity extends BaseLostSetupActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lostfindsetup1);
    }

    @Override
    public void showNext() {
        startActivityAndFinishSelf(LostSetup2Activity.class);
    }

    public void showPrevious() {
    }
}
