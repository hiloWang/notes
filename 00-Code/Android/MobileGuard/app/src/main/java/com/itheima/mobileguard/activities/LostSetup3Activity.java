package com.itheima.mobileguard.activities;

import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.itheima.mobileguard.R;

public class LostSetup3Activity extends BaseLostSetupActivity {
		private EditText et_setup3_contact;
		
		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_lostfindsetup3);
			et_setup3_contact = (EditText) findViewById(R.id.et_setup3_contact);
			String setsafeContact = sp.getString("safeContact", "");
			et_setup3_contact.setText(setsafeContact);
		}
		
		@Override
		public void showNext() {
			String safeContact = et_setup3_contact.getText().toString();
			if(TextUtils.isEmpty(safeContact)){
				Toast.makeText(this, "请先设置安全号码", Toast.LENGTH_LONG).show();
				return;
			}
			Editor editor = sp.edit();
			editor.putString("safeContact", safeContact);
			editor.commit();
			startActivityAndFinishSelf(LostSetup4Activity.class);
		}
		
		/**
		 * 按钮点击 选择联系人
		 * @param v
		 */
		public void selectContact(View v){
			Intent intent = new Intent(this,ListContactActivity.class);
			startActivityForResult(intent, 0);
		}
		/**
		 * 当联系人界面返回结果
		 */
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
			if(data != null)
			et_setup3_contact.setText(data.getStringExtra("phone"));
		}

		/**
		 * 返回上一个Activity
		 */
		@Override
		public void showPrevious() {
				startActivityAndFinishSelf(LostSetup2Activity.class);
		}
}
