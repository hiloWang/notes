package com.itheima.mobileguard.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;

/**
 * 只要手机的SIM变化了 就会重启 这时判断原有的sim卡的序列号是否和开机后的sim一样，不一样就说明sim
 * 卡变更了 ，就应该发送短信给用户设置的安全号码
 *
 * @author Administrator
 */
public class BootCompletedReceiver extends BroadcastReceiver {

    private SharedPreferences sp;
    private TelephonyManager tm;

    public void onReceive(Context context, Intent intent) {
        //获取偏好设置
        sp = context.getSharedPreferences("lostFindConfig", Context.MODE_PRIVATE);
        tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        //看用户是否开启了手机防盗
        boolean isOpen = sp.getBoolean("protecting", false);
        if (isOpen) {
            //说明用户已经开启了防盗
            //用户之前设置sim卡序列号 和 现在的序列号比较 如果不一样 就发送警告

            if (!sp.getString("sim", "").equals(tm.getSimSerialNumber() + "S")) {
                //发送短信
                //获取之前设置的安全号码
                String number = sp.getString("safeContact", "");
                SmsManager sm = SmsManager.getDefault();
                sm.sendTextMessage(number, null, "SIM card is changed !!!", null, null);
            }
        }
    }

}
