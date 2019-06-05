package com.itheima.mobileguard.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobileguard.R;

public class SettingView extends RelativeLayout {
	
	private CheckBox cb_setting;
	private TextView tv_desc;
	private TextView tv_title;
	/**
	 * 条目的描述信息
	 */
	private String[] descs;
	/**
	 * 标题
	 */
	private String title;
	public SettingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		System.out.println("我是3");
		init(context);
	}

	public SettingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.descs = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itheima.mobileguard", "descs").split("###");
		this.title = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.itheima.mobileguard", "title");
		init(context);
	}

	public SettingView(Context context) {
		super(context);
		System.out.println("我是 1");

		init(context);
	}

	private void init(Context context) {
		View view = View.inflate(context, R.layout.item_setting, null);
		this.addView(view);
		this.setBackgroundResource(R.drawable.item_selector);
		cb_setting = (CheckBox) findViewById(R.id.cb_setting);
		tv_desc = (TextView) findViewById(R.id.tv_setting_desc);
		tv_title = (TextView) findViewById(R.id.tv_setting_title);
		setTitle(title);
		setDescrition(descs, cb_setting.isChecked());
	}

	/**
	 * 设置标题
	 * @param title
	 */
	public void setTitle(String title){
		if(title != null)
		tv_title.setText(title);
	}
	/**
	 * 设置描述有两种状态 应当是一个string数组，这里只需要传入数组，由成员变量记住，
	 * 在点击items是进行设置
	 * @param descs
	 */
	public void setDescrition(String[] descs,boolean checked){
		this.descs = descs;
		setChecked(checked);
	}
	
	/**
	 * 是否被选中
	 * @return
	 */
	public boolean isChecked(){
		return cb_setting.isChecked();
	}
	/**
	 * 设置是否被选中
	 * @param checked
	 */
	public void setChecked(boolean checked){
		cb_setting.setChecked(checked);
		if(checked){
			tv_desc.setTextColor(0xFF005500);
			if(descs != null && descs.length>0 && descs[0] != null){
				tv_desc.setText(descs[0]);
			}
		}else{
			tv_desc.setTextColor(0x99000000);
			if(descs != null && descs.length>1 && descs[1] != null){
				tv_desc.setText(descs[1]);
			}
		}
	}
}
