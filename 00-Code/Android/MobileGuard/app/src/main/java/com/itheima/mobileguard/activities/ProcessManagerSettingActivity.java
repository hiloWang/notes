package com.itheima.mobileguard.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.services.ClearProcessService;
import com.itheima.mobileguard.utils.SystemInfoUtils;

public class ProcessManagerSettingActivity extends Activity {
		private SharedPreferences sp;
		private CheckBox cb_showSystemProcess;
		private CheckBox cb_process_clear;
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_process_manager_setting);
			cb_showSystemProcess = (CheckBox) findViewById(R.id.cb_process_setting);
			sp = getSharedPreferences("config", MODE_PRIVATE);
			cb_showSystemProcess.setChecked(sp.getBoolean("showSystemProcess", true));
			cb_process_clear = (CheckBox) findViewById(R.id.cb_process_clear);
			
			cb_showSystemProcess.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						if(cb_showSystemProcess.isChecked()){
							sp.edit().putBoolean("showSystemProcess", true).commit();
						}else{
							sp.edit().putBoolean("showSystemProcess", false).commit();
						}
				}
			});
			
			cb_process_clear.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							Intent intent = new Intent(ProcessManagerSettingActivity.this,ClearProcessService.class);
							if(cb_process_clear.isChecked()){
								startService(intent);
							}else{
								stopService(intent);
							}
				}
			});
			
			boolean isClear = SystemInfoUtils.isServiceRunning(this, "com.itheima.mobileguard.services.ClearProcessService");
			cb_process_clear.setChecked(isClear);
		}
}
