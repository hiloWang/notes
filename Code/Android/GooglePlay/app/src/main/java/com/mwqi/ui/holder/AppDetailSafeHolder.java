package com.mwqi.ui.holder;

import android.graphics.Color;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.mwqi.R;
import com.mwqi.bean.AppInfo;
import com.mwqi.http.image.ImageLoader;
import com.mwqi.utils.UIUtils;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;

import java.util.List;

/**
 * Created by mwqi on 2014/6/7.
 */
public class AppDetailSafeHolder extends BaseHolder<AppInfo> implements OnClickListener {
	private RelativeLayout mSafeLayout;
	private LinearLayout mContentLayout;
	private ImageView mArrow;

	private ImageView[] mIv;
	private ImageView[] mDesIv;
	private TextView[] mDesTv;
	private LinearLayout[] mLayout;

	@Override
	protected View initView() {
		View view = UIUtils.inflate(R.layout.app_detail_safe);
		mSafeLayout = (RelativeLayout) view.findViewById(R.id.safe_layout);
		mContentLayout = (LinearLayout) view.findViewById(R.id.safe_content);
		mContentLayout.getLayoutParams().height = 0;
		mArrow = (ImageView) view.findViewById(R.id.safe_arrow);
		mArrow.setTag(false);
		mSafeLayout.setOnClickListener(this);

		mIv = new ImageView[4];
		mIv[0] = (ImageView) view.findViewById(R.id.iv_1);
		mIv[1] = (ImageView) view.findViewById(R.id.iv_2);
		mIv[2] = (ImageView) view.findViewById(R.id.iv_3);
		mIv[3] = (ImageView) view.findViewById(R.id.iv_4);

		mDesIv = new ImageView[4];
		mDesIv[0] = (ImageView) view.findViewById(R.id.des_iv_1);
		mDesIv[1] = (ImageView) view.findViewById(R.id.des_iv_2);
		mDesIv[2] = (ImageView) view.findViewById(R.id.des_iv_3);
		mDesIv[3] = (ImageView) view.findViewById(R.id.des_iv_4);

		mDesTv = new TextView[4];
		mDesTv[0] = (TextView) view.findViewById(R.id.des_tv_1);
		mDesTv[1] = (TextView) view.findViewById(R.id.des_tv_2);
		mDesTv[2] = (TextView) view.findViewById(R.id.des_tv_3);
		mDesTv[3] = (TextView) view.findViewById(R.id.des_tv_4);

		mLayout = new LinearLayout[4];
		mLayout[0] = (LinearLayout) view.findViewById(R.id.des_layout_1);
		mLayout[1] = (LinearLayout) view.findViewById(R.id.des_layout_2);
		mLayout[2] = (LinearLayout) view.findViewById(R.id.des_layout_3);
		mLayout[3] = (LinearLayout) view.findViewById(R.id.des_layout_4);
		return view;
	}

	@Override
	public void refreshView() {
		AppInfo info = getData();
		List<String> safeUrl = info.getSafeUrl();
		List<String> safeDesUrl = info.getSafeDesUrl();
		List<String> safeDes = info.getSafeDes();
		List<Integer> safeDesColor = info.getSafeDesColor();

		for (int i = 0; i < 4; i++) {
			if (i < safeUrl.size() && i < safeDesUrl.size() && i < safeDes.size() && i < safeDesColor.size()) {
				ImageLoader.load(mIv[i], safeUrl.get(i));
				ImageLoader.load(mDesIv[i], safeDesUrl.get(i));
				mDesTv[i].setText(safeDes.get(i));

				int color;
				int colorType = safeDesColor.get(i);
				if (colorType >= 1 && colorType <= 3) {
					color = Color.rgb(255, 153, 0);
				} else if (colorType == 4) {
					color = Color.rgb(0, 177, 62);
				} else {
					color = Color.rgb(122, 122, 122);
				}
				mDesTv[i].setTextColor(color);

				mIv[i].setVisibility(View.VISIBLE);
				mLayout[i].setVisibility(View.VISIBLE);
			} else {
				mIv[i].setVisibility(View.GONE);
				mLayout[i].setVisibility(View.GONE);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.safe_layout:
				expand();
				break;
			default:
				break;
		}
	}

	private void expand() {
		final LayoutParams params = mContentLayout.getLayoutParams();
		int targetHeight;
		int height = mContentLayout.getMeasuredHeight();
		boolean flag = (Boolean) mArrow.getTag();
		if (flag) {
			mArrow.setTag(false);
			targetHeight = 0;
		} else {
			mArrow.setTag(true);
			targetHeight = measureContentHeight();
		}

		mSafeLayout.setEnabled(false);

		ValueAnimator va = ValueAnimator.ofInt(height, targetHeight);
		va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator va) {
				params.height = (Integer) va.getAnimatedValue();
				mContentLayout.setLayoutParams(params);
			}
		});
		va.addListener(new AnimatorListener() {
			@Override
			public void onAnimationStart(Animator arg0) {

			}

			@Override
			public void onAnimationRepeat(Animator arg0) {

			}

			@Override
			public void onAnimationEnd(Animator arg0) {
				boolean flag = (Boolean) mArrow.getTag();
				mArrow.setImageResource(flag ? R.drawable.arrow_up : R.drawable.arrow_down);
				mSafeLayout.setEnabled(true);
			}

			@Override
			public void onAnimationCancel(Animator arg0) {

			}
		});
		va.setDuration(300);
		va.start();
	}

	private int measureContentHeight() {
		int width = mContentLayout.getMeasuredWidth();
		mContentLayout.getLayoutParams().height = LayoutParams.WRAP_CONTENT;
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(1000, MeasureSpec.AT_MOST);
		mContentLayout.measure(widthMeasureSpec, heightMeasureSpec);
		return mContentLayout.getMeasuredHeight();
	}
}
