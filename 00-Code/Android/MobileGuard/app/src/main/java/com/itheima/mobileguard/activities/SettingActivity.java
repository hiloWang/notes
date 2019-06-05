package com.itheima.mobileguard.activities;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningServiceInfo;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.services.CallsafeService;
import com.itheima.mobileguard.services.ShowLoacationService;
import com.itheima.mobileguard.services.WatchDagService;
import com.itheima.mobileguard.ui.SettingView;
import com.itheima.mobileguard.utils.SystemInfoUtils;

public class SettingActivity extends Activity {
	private SharedPreferences sp;
	private SettingView sv_setting_blacknumber;
	private SettingView sv_setting_autoupdate;
	private SettingView sv_setting_showlocation;
	private ActivityManager am;
	private RelativeLayout rl_setting_changestyle;
	private TextView tv_setting_styledesc;
	private SettingView sv_setting_applock;
	
	//土司框风格
	private String[] items = {"半透明","活力橙","卫士蓝","金属灰","苹果绿"};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		// 自动更新设置
		sv_setting_autoupdate = (SettingView) findViewById(R.id.sv_setting_autoupdate);
		sv_setting_autoupdate.setChecked(sp.getBoolean("autoUpdate", false));
		sv_setting_autoupdate.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Boolean checked = sv_setting_autoupdate.isChecked();
				Editor editor = sp.edit();
				if (checked) {
					sv_setting_autoupdate.setChecked(false);
					editor.putBoolean("autoUpdate", false);
				} else {
					sv_setting_autoupdate.setChecked(true);
					editor.putBoolean("autoUpdate", true);
				}
				editor.commit();
			}
		});
		
		//黑名单拦截
		sv_setting_blacknumber = (SettingView) findViewById(R.id.sv_setting_blacknumber);
		sv_setting_blacknumber.setOnClickListener(new OnClickListener() {
			Intent intent = new Intent(SettingActivity.this,CallsafeService.class);
			public void onClick(View v) {
				Boolean checked = sv_setting_blacknumber.isChecked();
				if (checked) {
					sv_setting_blacknumber.setChecked(false);
					stopService(intent);
				} else {
					sv_setting_blacknumber.setChecked(true);
					startService(intent);
				}
			}
		});
		
		//来电显示
		sv_setting_showlocation = (SettingView) findViewById(R.id.sv_setting_showlocation);
		sv_setting_showlocation.setOnClickListener(new OnClickListener() {
			Intent intent = new Intent(SettingActivity.this,ShowLoacationService.class);
			public void onClick(View v) {
				Boolean checked = sv_setting_showlocation.isChecked();
				if (checked) {
					sv_setting_showlocation.setChecked(false);
					stopService(intent);
				} else {
					sv_setting_showlocation.setChecked(true);
					startService(intent);
				}
			}
		});
		tv_setting_styledesc  = (TextView) findViewById(R.id.tv_setting_styledesc);
		tv_setting_styledesc.setText(items[sp.getInt("item", 0)]);
		
		//程序锁
		sv_setting_applock = (SettingView) findViewById(R.id.sv_setting_applock);
		sv_setting_applock.setOnClickListener(new OnClickListener() {
			Intent intent = new Intent(SettingActivity.this,WatchDagService.class);
			public void onClick(View v) {
					if(sv_setting_applock.isChecked()){
						sv_setting_applock.setChecked(false);
						stopService(intent);
					}else{
						sv_setting_applock.setChecked(true);
						startService(intent);
					}
			}
		});
	}
	
	/**
	 * 弹出提示框风格选中的对话框
	 * @param v
	 */
	public void changeStyle(View v){
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("归属地提示框风格").setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
					sp.edit().putInt("item", which).commit();
					tv_setting_styledesc.setText(items[which]);
					dialog.dismiss();
			}
		}).show();
	}
	
	

	@Override
	protected void onStart() {
		//获取正在运行的服务
		List<RunningServiceInfo> list = am.getRunningServices(200);
		//判断服务的运行状态
				boolean running = SystemInfoUtils.isServiceRunning(this, "com.itheima.mobileguard.services.CallsafeService");
				if(running){
					sv_setting_blacknumber.setChecked(true);
				}else{ 
					sv_setting_blacknumber.setChecked(false);
				}
				
				running = SystemInfoUtils.isServiceRunning(this, "com.itheima.mobileguard.services.ShowLoacationService");
				if(running){
					sv_setting_showlocation.setChecked(true);
				}else{
					sv_setting_showlocation.setChecked(false);
				}
				
				running = SystemInfoUtils.isServiceRunning(this, "com.itheima.mobileguard.services.WatchDagService");
				if(running){
					sv_setting_applock.setChecked(true);
				}else{
					sv_setting_applock.setChecked(false);
				}
				
		super.onStart();
	}
	
}
