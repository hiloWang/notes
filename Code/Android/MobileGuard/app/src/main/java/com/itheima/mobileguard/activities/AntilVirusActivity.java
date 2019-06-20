package com.itheima.mobileguard.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.db.dao.AntiVirusDao;
import com.itheima.mobileguard.utils.MD5Utils;

public class AntilVirusActivity extends Activity {

	private List<String> packageNames;
	protected static final int BEGIN = 1;
	protected static final int SCANING = 2;
	protected static final int FINISH = 3;
	private ImageView iv_antivirus_scan;
	private TextView tv_antivirus_status;
	private ProgressBar pb_antivirus;
	private LinearLayout ll_antivirus_details;
	private Animation a;
	private PackageManager pm;
	private Button bt_clear;
	private boolean flag = true;
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.acitivity_antivirus);
		//初始化操作
		pm =getPackageManager();
		packageNames = new ArrayList<String>();
		iv_antivirus_scan = (ImageView) findViewById(R.id.iv_antivirus_scan);
		tv_antivirus_status = (TextView) findViewById(R.id.tv_antivirus_status);
		pb_antivirus = (ProgressBar) findViewById(R.id.pb_antivirus);
		ll_antivirus_details = (LinearLayout) findViewById(R.id.ll_antivirus_details);
		a = AnimationUtils.loadAnimation(this, R.anim.loading);
		bt_clear = (Button) findViewById(R.id.bt_clear);
	}
	 
	 Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			TextView child =null;
			ScanInfo info = null;
			String desc = null;
			switch (msg.what) {
			case BEGIN:
				tv_antivirus_status.setText("正在初始化8核心病毒库引擎...");
				break;
			case SCANING:
				info = (ScanInfo) msg.obj;
				desc = info.desc;
				child = new TextView(AntilVirusActivity.this);
				tv_antivirus_status.setText("正在扫描"+info.appName);
				if(desc == null){
					child.setText(info.appName+":扫描安全");
				}else{
					child.setTextColor(Color.RED);
					child.setText(info.appName+desc);
					packageNames.add(info.packageName);
				}
				ll_antivirus_details.addView(child, 0);
				break;
			case FINISH:
				iv_antivirus_scan.clearAnimation();
				tv_antivirus_status.setText("扫描完毕");
				if(packageNames.size() > 0){
					bt_clear.setClickable(true);
					Toast.makeText(getApplicationContext(), "扫描到"+packageNames.size()+"个病毒,建议立即清除", Toast.LENGTH_LONG).show();
				}else{
					Toast.makeText(getApplicationContext(), "恭喜您，您的手机没有任何病毒", Toast.LENGTH_LONG).show();
				}
				break;
			}
		} 
	 };
	 
	 public void startScan(View v){
		 //开启动画
		 iv_antivirus_scan.startAnimation(a);
		 ll_antivirus_details.removeAllViews();
		 //获取所有的包的信息
		//病毒查杀是耗时的操作 子线程中执行
		new Thread(){
			public void run(){
				Message msg = Message.obtain();
				msg.what = BEGIN;//开始扫描
				handler.sendMessage(msg);
				List<PackageInfo>  infos =  pm.getInstalledPackages(0);
				pb_antivirus.setMax(infos.size());
				int process = 1;
				String temp = null;
				ScanInfo scanInfo = new ScanInfo();
				for(PackageInfo info : infos){
					if (!flag) {
						return;
					}
					pb_antivirus.setProgress(process++);
					//获取程序的md5码
					temp = MD5Utils.getFileMd5Code(info.applicationInfo.sourceDir);
					System.out.println(info.applicationInfo.loadLabel(pm).toString()+":"+temp);
					//判断改程序是否是病毒
					temp = AntiVirusDao.check(getApplicationContext(), temp);
					 msg = Message.obtain();
					msg.what = SCANING;
					scanInfo.appName = info.applicationInfo.loadLabel(pm).toString();
					scanInfo.desc = temp;
					scanInfo.packageName = info.packageName;
					msg.obj = scanInfo;
					handler.sendMessage(msg);
					
				}
				//for循环完毕发送消息
				 msg = Message.obtain();
				msg.what = FINISH;
				handler.sendMessage(msg);
			}
		}.start();
	 }
	 
	 private class ScanInfo{
		 String appName;
		 String packageName;
		 String desc;
	 }
	 
	 public void clearVirus(View v){
		 
	 }
	 
	 @Override
	protected void onDestroy() {
		 flag = false;
		 super.onDestroy();
	}
}























