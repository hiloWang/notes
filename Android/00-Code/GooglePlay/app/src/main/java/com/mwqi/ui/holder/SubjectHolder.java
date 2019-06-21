package com.mwqi.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.mwqi.R;
import com.mwqi.bean.SubjectInfo;
import com.mwqi.http.image.ImageLoader;
import com.mwqi.ui.activity.BaseActivity;
import com.mwqi.utils.UIUtils;

/**
 * Created by mwqi on 2014/6/7.
 */
public class SubjectHolder extends BaseHolder<SubjectInfo> {

	private ImageView iv;
	private TextView tv;

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.subject_item);
		iv = (ImageView) view.findViewById(R.id.item_icon);
		tv = (TextView) view.findViewById(R.id.item_txt);
		return view;
	}

	@Override
	public void refreshView() {
		SubjectInfo data = getData();
		String des = data.getDes();
		String url = data.getUrl();
		tv.setText(des);
		iv.setTag(url);
		ImageLoader.load(iv, url);
	}

	@Override
	public void recycle() {
		recycleImageView(iv);
	}
}
