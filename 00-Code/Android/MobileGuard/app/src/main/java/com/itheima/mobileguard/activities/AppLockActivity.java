package com.itheima.mobileguard.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.fragments.UnlockAppFragment;
import com.itheima.mobileguard.fragments.LockedAppFragment;

public class AppLockActivity extends FragmentActivity implements OnClickListener {

    private TextView tv_applock;
    private TextView tv_appunlock;
    private FragmentManager fm;
    FragmentTransaction ft;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);

        fm = getSupportFragmentManager();
        tv_applock = (TextView) findViewById(R.id.tv_applock);
        tv_appunlock = (TextView) findViewById(R.id.tv_appunlock);

        ft = fm.beginTransaction();//开启
        ft.replace(R.id.fl_app_lock, new UnlockAppFragment());
        ft.commit();

        tv_applock.setOnClickListener(this);
        tv_appunlock.setOnClickListener(this);
    }

    /**
     * 选择加锁和未加锁的程序的界面
     */
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_appunlock:
                tv_appunlock.setBackgroundResource(R.drawable.tab_left_pressed);
                tv_applock.setBackgroundResource(R.drawable.tab_right_default);
                ft = fm.beginTransaction();//开启
                ft.replace(R.id.fl_app_lock, new UnlockAppFragment());
                ft.commit();
                break;
            case R.id.tv_applock:
                tv_appunlock.setBackgroundResource(R.drawable.tab_left_default);
                tv_applock.setBackgroundResource(R.drawable.tab_right_pressed);
                ft = fm.beginTransaction();//开启
                ft.replace(R.id.fl_app_lock, new LockedAppFragment());
                ft.commit();
                break;
        }
    }
}
