package com.mwqi.ui.holder;

import android.view.View;
import android.widget.ImageView;
import com.mwqi.R;
import com.mwqi.bean.AppInfo;
import com.mwqi.http.image.ImageLoader;
import com.mwqi.ui.activity.BaseActivity;
import com.mwqi.utils.UIUtils;

/**
 * Created by mwqi on 2014/6/7.
 */
public class AppDetailScreenHolder extends BaseHolder<AppInfo> {
	private ImageView[] mIv;

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.app_detail_screen);
		mIv = new ImageView[5];
		mIv[0] = (ImageView) view.findViewById(R.id.screen_1);
		mIv[1] = (ImageView) view.findViewById(R.id.screen_2);
		mIv[2] = (ImageView) view.findViewById(R.id.screen_3);
		mIv[3] = (ImageView) view.findViewById(R.id.screen_4);
		mIv[4] = (ImageView) view.findViewById(R.id.screen_5);
		return view;
	}

	@Override
	public void refreshView() {
		AppInfo info = getData();
		for (int i = 0; i < 5; i++) {
			if (i < info.getScreen().size()) {
				ImageLoader.load(mIv[i], info.getScreen().get(i));
				mIv[i].setVisibility(View.VISIBLE);
			} else {
				mIv[i].setVisibility(View.GONE);
			}
		}
	}
}
