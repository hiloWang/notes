package com.itheima.mobileguard.activities;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.SharedPreferences.Editor;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.utils.MD5Utils;
import com.itheima.mobileguard.utils.SmsUtils;
import com.itheima.mobileguard.utils.SmsUtils.SmsBackupCallBack;
import com.itheima.mobileguard.utils.SmsUtils.SmsRestoreCallBack;
import com.itheima.mobileguard.utils.UiUtils;

public class AtoolsActivity extends Activity {

	private SharedPreferences sp;
	
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			sp = getSharedPreferences("config", MODE_PRIVATE);
			setContentView(R.layout.activity_atools);
		}
		
		/**
		 * 来电查询 打开来电查询activity
		 * @param v
		 */
		public void  queryNumberAddress(View v){
			Intent intent = new Intent(this,QueryNumberAddressActivity.class	);
			startActivity(intent);
		}
		/**
		 * 短信的备份
		 */
		public void smsBackup(View v){
			final ProgressDialog pd = new ProgressDialog(this);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//水平方向
			pd.setTitle("备份提示");
			pd.show();
			new Thread(){
				public void run(){
					try {
					boolean result = 	SmsUtils.backupSms(AtoolsActivity.this,new SmsBackupCallBack() {
						
						public void onStartBackup(final int size) {
									pd.setMax(size);
						}
						
						public void onBackuping(final int process) {
									pd.setProgress(process);
						}
					});
					if(result){
						UiUtils.showToast(AtoolsActivity.this, "备份成功");
					}
					} catch (IllegalArgumentException e) {
						UiUtils.showToast(AtoolsActivity.this, "短信备份失败");
						e.printStackTrace();
					} catch (IllegalStateException e) {
						UiUtils.showToast(AtoolsActivity.this, "sd卡不存在或者空间不够");
						e.printStackTrace();
					}catch(FileNotFoundException e){
						UiUtils.showToast(AtoolsActivity.this, "文件生成失败");
					}catch (IOException e) {
						UiUtils.showToast(AtoolsActivity.this, "文件读写错误");
						e.printStackTrace();
					}finally{
						pd.dismiss();
					}
				}
			}.start();
		}
		/**
		 * 短信的还原
		 */
		public void smsRestore(View v){
			AlertDialog.Builder builder = new Builder(this);
			builder.setTitle("还原提示").setMessage("还原前会删除现有短信，是否还原")
			.setPositiveButton("开始还原", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					SmsUtils.deleteSms(getApplicationContext());
					runResotre();
				}
			}).setNegativeButton("取消", new OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					Toast.makeText(AtoolsActivity.this, "取消还原", Toast.LENGTH_LONG).show();
				}
			}).show();
			
		}
		
		private void runResotre(){
			final ProgressDialog pd = new ProgressDialog(this);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//水平方向
			pd.setTitle("备份提示");
			pd.show();
			new Thread(){
				public void run(){
					try {
							boolean result = SmsUtils.resotreSms(getApplicationContext(), new SmsRestoreCallBack() {
							
							@Override
							public void onStartRestore(int size) {
									pd.setMax(size);
							}
							
							@Override
							public void onRestoreing(int process) {
									pd.setProgress(process);
							}
						});
							if(result){
									UiUtils.showToast(AtoolsActivity.this, "还原成功");
							}
					} catch (XmlPullParserException e) {
						UiUtils.showToast(AtoolsActivity.this, "解析文件失败");
						e.printStackTrace();
					} catch (IOException e) {
						UiUtils.showToast(AtoolsActivity.this, "读取文件失败");
						e.printStackTrace();
					}catch (IllegalStateException e) {
						UiUtils.showToast(AtoolsActivity.this, "您没有备份过短信或者备份文件已丢失");
					}finally{
						pd.dismiss();
					}
				}
			}.start();
		}
		
		private AlertDialog dialog;
		/**
		 * 程序锁
		 * @param v
		 */
		public void appLock(View v){
//			打开一个新的界面  程序锁界面
			if("".equals(sp.getString("applockPws", ""))){
					View view = View.inflate(this, R.layout.dialog_atools_setuppassword, null);
					AlertDialog.Builder builder = new Builder(this);
					dialog = builder.create();
					final EditText et_pwd = (EditText) view.findViewById(R.id.et_atools_setuppassword);
					final EditText et_atools_confirmpassword = (EditText) view.findViewById(R.id.et_atools_confirmpassword);
					Button bt_cancel = (Button) view.findViewById(R.id.bt_atools_cancel);
					Button bt_ok = (Button) view.findViewById(R.id.bt_atools_ok);
					dialog.setView(view, 0, 0, 0, 0);
					
					bt_ok.setOnClickListener(new View.OnClickListener() {
						public void onClick(View v) {
							// 现获取用户的输入
							String newPassword = et_pwd.getText().toString();
							String confirmPassowrd = et_atools_confirmpassword.getText()
									.toString();
							if (TextUtils.isEmpty(newPassword)
									|| TextUtils.isEmpty(confirmPassowrd)) {
								UiUtils.showToast(AtoolsActivity.this, "密码和确认密码都必须输入");
								return;
							}
							if (!newPassword.equals(confirmPassowrd)) {
								UiUtils.showToast(AtoolsActivity.this, "两次输入不一致");
								return;
							}
							Editor editor = sp.edit();
							editor.putString("applockPws", MD5Utils.MD5Encode(newPassword));
							editor.commit();
							loadLockAppActivity();
							dialog.dismiss();
						}
					});
					bt_cancel.setOnClickListener(new View.OnClickListener() {// 取消了设置
								public void onClick(View v) {
									dialog.dismiss();
								}
							});
					dialog.show();
			}else{
				loadLockAppActivity();
			}
		}

		private void loadLockAppActivity() {
				Intent intent = new Intent(this,AppLockActivity.class);
				startActivity(intent);
		}
}
