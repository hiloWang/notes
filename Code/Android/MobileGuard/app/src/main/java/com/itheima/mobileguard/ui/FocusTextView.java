package com.itheima.mobileguard.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FocusTextView extends TextView {

	public FocusTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	public FocusTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public FocusTextView(Context context) {
		super(context);
	}
	
	@Override//返回true 欺骗系统 一直得到焦点
	public boolean isFocused() {
		return true;
	}
}
