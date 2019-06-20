package com.mwqi.ui.fragment;

import java.util.List;
import java.util.Random;

import android.graphics.Color;
import com.mwqi.R;
import com.mwqi.http.protocol.HotProtocol;
import com.mwqi.ui.widget.FlowLayout;
import com.mwqi.ui.widget.LoadingPage.LoadResult;
import com.mwqi.utils.DrawableUtils;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import com.mwqi.utils.UIUtils;

public class HotFragment extends BaseFragment {
	private FlowLayout mLayout;
	private List<String> mDatas;

	@Override
	protected LoadResult load() {
		HotProtocol protocol = new HotProtocol();
		mDatas = protocol.load(0);
		return check(mDatas);
	}

	@Override
	protected View createLoadedView() {
		//防止在小屏手机上显示不全，需要通过scrollView包裹主界面
		ScrollView mScrollView = new ScrollView(UIUtils.getContext());
		mScrollView.setFillViewport(true);//设置可以填充父窗体
		//初始化布局，该布局可以自动分配子View位置，保持每一行都能对齐
		mLayout = new FlowLayout(UIUtils.getContext());
		mLayout.setBackgroundResource(R.drawable.grid_item_bg_normal);
		int layoutPadding = UIUtils.dip2px(13);
		mLayout.setPadding(layoutPadding, layoutPadding, layoutPadding, layoutPadding);
		mLayout.setHorizontalSpacing(layoutPadding);
		mLayout.setVerticalSpacing(layoutPadding);

		int textPaddingV = UIUtils.dip2px(4);
		int textPaddingH = UIUtils.dip2px(7);
		int backColor = 0xffcecece;
		int radius = UIUtils.dip2px(5);
		//代码动态创建一个图片
		/**
		 * 第一个参数的填充的颜色，第二个参数是描边的颜色，第三个参数是圆角
		 */
		GradientDrawable pressDrawable = DrawableUtils.createDrawable(backColor, backColor, radius);
		Random mRdm = new Random();
		for (int i = 0; i < mDatas.size(); i++) {
			TextView tv = new TextView(UIUtils.getContext());
			// 随机颜色的范围0x202020~0xefefef
			int red = 32 + mRdm.nextInt(208);
			int green = 32 + mRdm.nextInt(208);
			int blue = 32 + mRdm.nextInt(208);
			int color = 0xff000000 | (red << 16) | (green << 8) | blue;
			//设置圆角
			GradientDrawable normalDrawable = DrawableUtils.createDrawable(color, backColor, radius);
			//创建背景图片选择器
			StateListDrawable selector = DrawableUtils.createSelector(normalDrawable, pressDrawable);
			tv.setBackgroundDrawable(selector);
			
			final String text = mDatas.get(i);
			tv.setText(text);
			tv.setTextColor(Color.rgb(255,255,255));
			tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
			tv.setGravity(Gravity.CENTER);
			tv.setPadding(textPaddingH, textPaddingV, textPaddingH, textPaddingV);
			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					UIUtils.showToastSafe(text);
				}
			});
			mLayout.addView(tv);
		}
		mScrollView.addView(mLayout);
		return mScrollView;
	}
}
