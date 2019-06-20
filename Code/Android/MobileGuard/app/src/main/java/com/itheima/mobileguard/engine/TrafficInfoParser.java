package com.itheima.mobileguard.engine;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.itheima.mobileguard.domain.TrafficInfo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.content.pm.ResolveInfo;
import android.net.TrafficStats;

/**
 * 用于获取网络流量的信息
 * @author Administrator
 *
 */
public class TrafficInfoParser {

		//获取所以可以启动的APP
	public static List<TrafficInfo> findLauncherTrafficInfo(Context context){
		Intent intent = new Intent();
		PackageManager pm = context.getPackageManager();
		intent.setAction(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		//resolverInfo :相当于一个IntentFilter 记录其信息
		List<TrafficInfo> traffiicinfos = new ArrayList<TrafficInfo>();
		List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);//只匹配默认的
		TrafficInfo info = null;
		for(ResolveInfo resolverInfo : infos){
			info = new TrafficInfo();
			info.setAppName(resolverInfo.activityInfo.applicationInfo.loadLabel(pm).toString());
			info.setIcon(resolverInfo.activityInfo.applicationInfo.loadIcon(pm));
			info.setPackageName(resolverInfo.activityInfo.packageName);
			info.setUid(resolverInfo.activityInfo.applicationInfo.uid);
			traffiicinfos.add(info);
			info = null;
		}
		return traffiicinfos;
	}
	
	/**
	 * 获取所有可以上网的app  即具有internet权限的APP
	 */
	public static List<TrafficInfo> findInternetApp(Context context){
		PackageManager pm = context.getPackageManager();
		//获取一个PackageInfo的集合  查询的是没有被卸载的和 还需要获取其权限
		List<PackageInfo>  packageInfos = pm.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES | PackageManager.GET_PERMISSIONS);
		TrafficInfo trafficInfo = null;
		List<TrafficInfo> traffiicinfos = new ArrayList<TrafficInfo>();
		for(PackageInfo info: packageInfos){
			String[]  ps = info.requestedPermissions;
			if(ps != null)//有的应用可能没有权限
			for(String permission: ps){
				if(permission.equals(Manifest.permission.INTERNET)){
					trafficInfo = new TrafficInfo();
					trafficInfo.setAppName(info.applicationInfo.loadLabel(pm).toString());
					trafficInfo.setPackageName(info.packageName);
					trafficInfo.setIcon(info.applicationInfo.loadIcon(pm));
					trafficInfo.setUid(info.applicationInfo.uid);
					trafficInfo.setDownTraffic(TrafficStats.getUidRxBytes(info.applicationInfo.uid));
					trafficInfo.setUpTraffic(TrafficStats.getUidTxBytes(info.applicationInfo.uid));
					traffiicinfos.add(trafficInfo);
					trafficInfo = null;
			}
			}
		}
		return traffiicinfos;
	}
}
