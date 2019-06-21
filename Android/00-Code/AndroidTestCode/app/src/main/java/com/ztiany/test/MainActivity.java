package com.ztiany.test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ztiany.test.activity_animation.AnimActivity;
import com.ztiany.test.anr.ANRActivity;
import com.ztiany.test.fragments.FragmentsActivity;
import com.ztiany.test.fragments.ViewPagerActivity;
import com.ztiany.test.status.StatusBarActivity;
import com.ztiany.test.time.TimeActivity;
import com.ztiany.test.tint.TintActivity;

public class MainActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void recreate(View view) {
        recreate();
    }

    public void opActivityAnim(View view) {
        startActivity(new Intent(this, AnimActivity.class));
    }

    public void openTint(View view) {
        startActivity(new Intent(this, TintActivity.class));
    }

    public void openTime(View view) {
        startActivity(new Intent(this, TimeActivity.class));
    }

    public void openStatus(View view) {
        startActivity(new Intent(this, StatusBarActivity.class));
    }

    public void fragments(View view) {
        startActivity(new Intent(this, FragmentsActivity.class));
    }

    public void fragmentsViewPager(View view) {
        startActivity(new Intent(this, ViewPagerActivity.class));
    }

    public void makeANR(View view) {
        startActivity(new Intent(this, ANRActivity.class));

    }
}

