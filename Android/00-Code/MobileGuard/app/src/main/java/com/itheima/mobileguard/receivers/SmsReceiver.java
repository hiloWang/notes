package com.itheima.mobileguard.receivers;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.activities.TackPictureActivity;

/**
 * 监听接受到的短信 如果是特殊指令就执行对应的操作
 *
 * @author Administrator
 */
public class SmsReceiver extends BroadcastReceiver {
    private SharedPreferences sp;
    private ComponentName cn;

    public void onReceive(Context context, Intent intent) {


        // 判断用户是否开启了防盗功能
        // 获取偏好设置
        sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
        // 看用户是否开启了手机防盗
        boolean isOpen = sp.getBoolean("protecting", false);

        if (isOpen) {
            cn = new ComponentName(context, MyDeviceAdmin.class);
            // 获取短信内容
            /**
             * 公共接口，用于管理强制执行的设备上的政策。此类的大多数客户必须公布用户目前已经启用了DeviceAdminReceiver。
             *
             * 开发人员指南 有关管理政策，以获取设备adminstration的更多信息，请阅读设备管理开发人员指南。
             */
            DevicePolicyManager dpm = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
            Object[] objs = (Object[]) intent.getExtras().get("pdus");

            // 遍历短信数组
            for (Object obj : objs) {
                SmsMessage sm = SmsMessage.createFromPdu((byte[]) obj);
                String smsBody = sm.getMessageBody();// 获取短信内容
                // 判断内容是否是特殊指令
                if ("#*location*#".equals(smsBody)) {
                    // 返回位置 由于获取位置可能是一个比较耗时的操作 不能放在广播接收者里面
                    // 因为广播接收者的生命周期很短 也不能方法在子线程里面 所以需要开启一个服务
                    // 去获取位置 并发送者手机主人
                    Intent i = new Intent(context, com.itheima.mobileguard.services.LoactionService.class);
                    context.startService(i);
                    System.out.println("lai le");
                    abortBroadcast();
                } else if ("#*alarm*#".equals(smsBody)) {
                    // 播放报警音乐
                    MediaPlayer play = MediaPlayer.create(context, R.raw.ylzs);
                    // 设置音量
                    play.setVolume(1.0f, 1.0f);
                    play.start();
                    abortBroadcast();
                } else if ("#*wipedata*#".equals(smsBody)) {
                    // TODO远程删除手机数据 报错存储卡的数据
                    if (dpm.isAdminActive(cn)) {
                        dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                    }
                    abortBroadcast();
                } else if ("#*lockscreen*#".equals(smsBody)) {
                    if (dpm.isAdminActive(cn)) {
                        dpm.resetPassword("123", 0);// 设置锁屏密码
                        dpm.lockNow();// 锁屏
                    }
                    abortBroadcast();// 拦截短信
                } else if ("#*tackpicture*#".equals(smsBody)) {
                    //远程拍照上传
                    Intent i = new Intent(context, TackPictureActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    abortBroadcast();
                    context.startActivity(i);
                }
            }
        }

    }
}
