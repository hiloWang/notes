package com.itheima.mobileguard.engine;

import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Debug.MemoryInfo;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.domain.ProcessInfo;

public class ProcessInfoParser {

	
	
	
	/**
	 * 获取系统正在运行的进程的信息
	 * @param context
	 * @return
	 */
	public static List<ProcessInfo> getRunningProcess(Context context){
		//获取进程管理器
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		List<RunningAppProcessInfo> runningAppProcessInfos =  am.getRunningAppProcesses();
		List<ProcessInfo> infos = new ArrayList<ProcessInfo>();//集合 存储javabean
		ProcessInfo info = null;
		for(RunningAppProcessInfo processInfo : runningAppProcessInfos){
				info = new ProcessInfo();
				String packageName = processInfo.processName;//得到包名
				info.setPackageName(packageName);//设置包名
				try {
					//得到应用名 有的程序没有appName 就是指为包名
					String appName = pm.getApplicationLabel(pm.getApplicationInfo(packageName, 0)).toString();
					info.setAppName(appName);//设置应用名
				} catch (NameNotFoundException e1) {
					e1.printStackTrace();
					info.setAppName(packageName);
				}
				try {
					//有的程序没有图标就设置为机器人图标
					Drawable icon = pm.getApplicationIcon(packageName);
					info.setIcon(icon); //设置图标
				} catch (NameNotFoundException e) {
					e.printStackTrace();//设置为默认图片
					info.setIcon(context.getResources().getDrawable(R.drawable.default_ic_launcher));
				}
				
				//获取一个进程占用的内存
				MemoryInfo[] memoryInfos = am.getProcessMemoryInfo(new int[]{processInfo.pid});
				info.setOccupyMemory(memoryInfos[0].getTotalPrivateDirty()*1024);//设置占用内存
				//是否是用户进程
				try {
					if((pm.getApplicationInfo(packageName, 0).flags & ApplicationInfo.FLAG_SYSTEM) != 0){
						info.setUserApp(false);
					}else{
						info.setUserApp(true);
					}
				} catch (NameNotFoundException e) {
					e.printStackTrace();
				}
				infos.add(info);
				info = null;
		}
		return infos;
	}
}
