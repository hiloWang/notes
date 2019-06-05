package com.itheima.mobileguard.activities;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.itheima.mobileguard.R;

public class LostSetup4Activity extends BaseLostSetupActivity {

    private CheckBox cb_setup4;
    private TextView tv_setup4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lostfindsetup4);

        cb_setup4 = (CheckBox) findViewById(R.id.cb_setup4);
        tv_setup4 = (TextView) findViewById(R.id.tv_setup4);

        boolean protecting = sp.getBoolean("protecting", false);
        if (protecting) {//设置了
            tv_setup4.setText("手机防盗已经开启");
            cb_setup4.setChecked(true);
        } else {
            tv_setup4.setText("手机防盗没有开启");
            cb_setup4.setChecked(false);
        }

        //注册复选框监听事件
        cb_setup4.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    tv_setup4.setText("手机防盗已经开启");
                } else {
                    tv_setup4.setText("手机防盗没有开启");
                }
                Editor editor = sp.edit();
                editor.putBoolean("protecting", isChecked);
                editor.commit();
            }
        });
    }

    /**
     * 完成设置进入到界面
     */
    public void showNext() {
        //不管用户是否设置了开启防盗 都向系统写一个配置信息 用户设置完成了 可以进入防盗界面
        Editor editor = sp.edit();
        editor.putBoolean("finishing", true);
        editor.commit();
        startActivityAndFinishSelf(LostFindActivity.class);
    }


    /**
     * 返回上一个Activity
     */
    @Override
    public void showPrevious() {
        startActivityAndFinishSelf(LostSetup3Activity.class);
    }
}
