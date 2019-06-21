package com.itheima.mobileguard.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.SmsManager;

/**
 * 获取位置侵犯到了用户的隐私 需要添加权限
 *
 * @author Administrator
 */
public class LoactionService extends Service {

    private LocationManager lm;
    private SharedPreferences sp;
    private MyLocationListener listener;

    @Override
    public void onCreate() {
        // 获取系统位置服务
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        // criteria 标准 true 使用可用的位置提供者
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);// 最精确的位置
        criteria.setCostAllowed(true);// 允许系统开销
        String name = lm.getBestProvider(criteria, true);
        System.out.println("最好的位置服务:" + name);
        // 定位服务名称 时间和间距采用系统默认即可 位置监听器
        listener = new MyLocationListener();
        lm.requestLocationUpdates(name, 0, 0, listener);
        super.onCreate();
    }


    /**
     * 位置监听器
     *
     * @author Administrator
     */
    private class MyLocationListener implements LocationListener {
        /**
         * 位置改变 被调用
         */
        public void onLocationChanged(Location location) {
            float accuracy = location.getAccuracy();//获取精确度
            double latitute = location.getLatitude();//获取维度
            double longitude = location.getLongitude();//获取经度
            float speed = location.getSpeed();//获取速率
            double altitude = location.getAltitude();//获取海拔
            //组织短信
            StringBuilder sb = new StringBuilder();
            System.out.println(sb.toString());
            sb.append("accuracy:" + accuracy + "\nlatitute:" + latitute
                    + "\nlongitude" + longitude + "\nspeed:" + speed);
            //发送短信给安全号码
            SmsManager sm = SmsManager.getDefault();
            sm.sendTextMessage(sp.getString("safeContact", ""), null, sb.toString(), null, null);
            //短信只发送一次 不然会发爆
            //结束自己
            stopSelf();
        }

        /**
         * Called when the provider status changes. This method is called when a
         * provider is unable to fetch a location or if the provider has
         * recently become available after a period of unavailability.
         * 当provider的状态改变，这个方法被掉用，
         */
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        public void onProviderEnabled(String provider) {

        }

        public void onProviderDisabled(String provider) {

        }
    }

    /**
     * 释放资源
     */
    @Override
    public void onDestroy() {
        lm.removeUpdates(listener);
        listener = null;
        super.onDestroy();
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

}
