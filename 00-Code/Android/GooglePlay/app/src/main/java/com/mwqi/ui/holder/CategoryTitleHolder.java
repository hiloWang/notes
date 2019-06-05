package com.mwqi.ui.holder;

import android.view.View;
import android.widget.TextView;
import com.mwqi.R;
import com.mwqi.bean.CategoryInfo;
import com.mwqi.ui.activity.BaseActivity;
import com.mwqi.utils.UIUtils;

/**
 * Created by mwqi on 2014/6/7.
 */
public class CategoryTitleHolder extends BaseHolder<CategoryInfo> {
	private TextView mTextView;

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.category_item_title);
		mTextView = (TextView) view.findViewById(R.id.tv_title);
		return view;
	}

	@Override
	public void refreshView() {
		mTextView.setText(getData().getTitle());
	}
}
