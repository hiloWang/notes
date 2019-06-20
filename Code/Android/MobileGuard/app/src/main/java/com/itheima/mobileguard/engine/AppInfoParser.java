package com.itheima.mobileguard.engine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.TrafficStats;

import com.itheima.mobileguard.domain.AppInfo;

public class AppInfoParser {

		public static List<AppInfo> getAllAppinfo(Context context){
			PackageManager pm = context.getPackageManager();
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			
			List<PackageInfo> infos = pm.getInstalledPackages(0);
			List<AppInfo> appInfos = new ArrayList<AppInfo>(); 
			
			AppInfo appInfo = null;
			for(PackageInfo pi:infos){
				
			
				appInfo = new AppInfo();
				appInfo.setPackageName(pi.packageName);//包名
				/*
				 * applicationInfo.sourceDir 是应用程序的安装路径
				 */
				appInfo.setAppSize(new File(pi.applicationInfo.sourceDir).length());
				appInfo.setApkPath(pi.applicationInfo.sourceDir);
				appInfo.setAppIcon(pi.applicationInfo.loadIcon(pm));//应用的图标
				appInfo.setAppName(pi.applicationInfo.loadLabel(pm).toString());//应用名
				
			
				/*
				 * 而二进制映射，用一个二进制数据来传递信息，可以传递多个信息
				 * 防止因为信息过多而造成代码冗长
				 * 
				 */
				if((ApplicationInfo.FLAG_EXTERNAL_STORAGE & pi.applicationInfo.flags)	!= 0){
					appInfo.setInRom(false);//在SD卡中
				}else{
					appInfo.setInRom(true);//在手机内存
				}
				if((ApplicationInfo.FLAG_SYSTEM & pi.applicationInfo.flags) != 0){
					appInfo.setUserApp(false);//系统应用
				}else{
					appInfo.setUserApp(true);//用户应用
				}
				appInfos.add(appInfo);
				appInfo = null;
			}
			return appInfos;
		}
}
