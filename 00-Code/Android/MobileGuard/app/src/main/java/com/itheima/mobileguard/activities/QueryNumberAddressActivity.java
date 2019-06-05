package com.itheima.mobileguard.activities;


import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AutoCompleteTextView.Validator;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobileguard.R;
import com.itheima.mobileguard.db.dao.NumberAddressDao;

public class QueryNumberAddressActivity extends Activity {
		private EditText et_number;
		private TextView tv_result;
		//震动服务
		private Vibrator vibrator;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_querynumberaddress);
			
			et_number = (EditText) findViewById(R.id.et_querynumberaddress);
			tv_result = (TextView) findViewById(R.id.tv_querynumberaddress);
			//得到系统震动服务
			vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			//内容变化的监听事件
			et_number.addTextChangedListener(new TextWatcher() {
				
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					String number = s.toString();
					if(TextUtils.isEmpty(number)){
						tv_result.setText("");
						return ;
					}
					String address = NumberAddressDao.query(number, QueryNumberAddressActivity.this);
					tv_result.setText(address);
				}
				public void beforeTextChanged(CharSequence s, int start, int count,
						int after) {
					
				}
				public void afterTextChanged(Editable s) {
					
				}
			});
		}
		
		
		public void query(View v){
			String number = et_number.getText().toString().trim();
			if(TextUtils.isEmpty(number)){
				Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
				
			/*	//自定义动画插入器
				shake.setInterpolator(new Interpolator() {
					public float getInterpolation(float input) {
						return 0;
					}
				});*/
				
				
		        et_number.startAnimation(shake);
				Toast.makeText(this, "请输入号码", Toast.LENGTH_LONG).show();
				
				//实现手机震动 需要权限 震动多少秒  停下多少秒  重复多少次
			//	vibrator.vibrate(new long[]{200,300,100,400,440,420}, 2);
			
				/*<uses-permission android:name="android.permission.VIBRATE"/>
				 * 有些厂商的手机振动器可能没有实现以上API 
				 * 只实现了 	vibrator.vibrate(milliseconds); 如果需要实现多次震动
				 * 可以利用多线程来解决
				 */
				
				return ;
			}
			String address = NumberAddressDao.query(number, this);
			tv_result.setText(address);
		}
		
}
