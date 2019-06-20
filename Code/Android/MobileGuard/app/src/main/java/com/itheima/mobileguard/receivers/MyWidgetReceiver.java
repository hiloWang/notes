package com.itheima.mobileguard.receivers;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

import com.itheima.mobileguard.services.UpdataWidgetService;

/**
 * 特殊的广播接收中
 *
 * @author Administrator
 */
public class MyWidgetReceiver extends AppWidgetProvider {

    /**
     * 等到框架来更新数据很慢 切不可靠 所以定义一个服务来定时更新
     * 这个方法是第一次创建widget是调用的
     */
    public void onEnabled(Context context) {
        //开启服务定期更新
        Intent intent = new Intent(context, UpdataWidgetService.class);
        context.startService(intent);
        super.onEnabled(context);
    }

    /**
     * 当最后一个widget被移除时调用
     * 最后关闭服务
     */
    public void onDisabled(Context context) {
        Intent intent = new Intent(context, UpdataWidgetService.class);
        context.stopService(intent);
        super.onDisabled(context);
    }
}
