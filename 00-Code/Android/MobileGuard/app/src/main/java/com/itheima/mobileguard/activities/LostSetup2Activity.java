package com.itheima.mobileguard.activities;

import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.itheima.mobileguard.R;

public class LostSetup2Activity extends BaseLostSetupActivity {
    private TelephonyManager tm;
    private ImageView iv_setup2;
    private String simNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lostfindsetup2);
        //获取通话服务
        tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        simNumber = tm.getSimSerialNumber();
        iv_setup2 = (ImageView) findViewById(R.id.iv_setup2);
        //判读用户使用设置绑定了SIM
        String sim = sp.getString("sim", "");
        //更新界面
        if (TextUtils.isEmpty(sim)) {//没有绑定sim
            iv_setup2.setImageResource(R.drawable.unlock);
        } else {//绑定过了
            iv_setup2.setImageResource(R.drawable.lock);
        }
    }

    public void bindSIMCrad(View v) {
        String sim = sp.getString("sim", "");
        if (TextUtils.isEmpty(sim)) {//没有绑定sim
            Editor editor = sp.edit();
            if (simNumber == null) {
                simNumber = "1KS932SL9DLJ099";
            }
            editor.putString("sim", simNumber);
            editor.commit();
            iv_setup2.setImageResource(R.drawable.lock);
            Toast.makeText(this, "绑定成功", Toast.LENGTH_SHORT).show();
        } else {//绑定过了
            Editor editor = sp.edit();
            editor.putString("sim", "");
            editor.commit();
            iv_setup2.setImageResource(R.drawable.unlock);
            Toast.makeText(this, "解绑成功", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // TODO Auto-generated method stub
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     * 进入下一个界面
     */
    @Override
    public void showNext() {
//			判断用户是否绑定了sim卡 没有病毒不能到下一个界面
        String sim = sp.getString("sim", "");
        if (TextUtils.isEmpty(sim)) {
            Toast.makeText(this, "请先绑定SIM卡", Toast.LENGTH_LONG).show();
            return;
        }
        startActivityAndFinishSelf(LostSetup3Activity.class);
    }

    /**
     * 返回上一个Activity
     */
    @Override
    public void showPrevious() {
        startActivityAndFinishSelf(LostSetup1Activity.class);
    }
}
