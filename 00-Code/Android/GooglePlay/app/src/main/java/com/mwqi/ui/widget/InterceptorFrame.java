package com.mwqi.ui.widget;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import com.mwqi.ui.activity.BaseActivity;
import com.mwqi.utils.UIUtils;
import com.mwqi.utils.ViewUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class InterceptorFrame extends FrameLayout {
	public static final int ORIENTATION_UP = 0x1;
	public static final int ORIENTATION_DOWN = 0x2;
	public static final int ORIENTATION_LEFT = 0x4;
	public static final int ORIENTATION_RIGHT = 0x8;
	public static final int ORIENTATION_ALL = 0x10;


	private List<View> mInterceptorViews;
	private Map<View, Integer> mViewAndOrientation;
	private int mTouchSlop;
	private float mLastX;
	private float mLastY;
	private View mTarget;

	public InterceptorFrame(Context context) {
		super(context);
		init();
	}

	private void init() {
		mInterceptorViews = new LinkedList<View>();
		mViewAndOrientation = new HashMap<View, Integer>();
		final ViewConfiguration configuration = ViewConfiguration.get(getContext());
		mTouchSlop = configuration.getScaledTouchSlop();
	}

	public void addInterceptorView(final View v, final int orientation) {
		UIUtils.runInMainThread(new Runnable() {
			@Override
			public void run() {
				if (!mInterceptorViews.contains(v)) {
					mInterceptorViews.add(v);
					mViewAndOrientation.put(v, orientation);
				}
			}
		});
	}

	public void removeInterceptorView(final View v) {
		UIUtils.runInMainThread(new Runnable() {
			@Override
			public void run() {
				mInterceptorViews.remove(v);
				mViewAndOrientation.remove(v);
			}
		});
	}

	private View isTouchInterceptedView(MotionEvent event, int orientation) {
		for (View v : mInterceptorViews) {
			if (ViewUtils.isTouchInView(event, v) && (mViewAndOrientation.get(v) & orientation) == orientation && v.dispatchTouchEvent(event)) {
				return v;
			}
		}
		return null;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		int action = ev.getAction();

		if (mTarget != null) {
			boolean flag = mTarget.dispatchTouchEvent(ev);
			if (flag && (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP)) {
				mTarget = null;
			}
			return flag;
		}

		final float x = ev.getX();
		final float y = ev.getY();
		View view = null;
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				mLastX = x;
				mLastY = y;
				view = isTouchInterceptedView(ev, ORIENTATION_ALL);
				break;
			case MotionEvent.ACTION_MOVE:
				final int xDiff = (int) Math.abs(x - mLastX);
				final int yDiff = (int) Math.abs(y - mLastY);
				if (xDiff > mTouchSlop && xDiff > yDiff) {
					view = isTouchInterceptedView(ev, (x - mLastX > 0) ? ORIENTATION_RIGHT : ORIENTATION_LEFT);
				} else if (yDiff > mTouchSlop && yDiff > xDiff) {
					view = isTouchInterceptedView(ev, (y - mLastY > 0) ? ORIENTATION_DOWN : ORIENTATION_UP);
				}
				break;
			case MotionEvent.ACTION_CANCEL:
			case MotionEvent.ACTION_UP:
				mTarget = null;
				break;
			default:
				break;
		}
		if (view != null) {
			mTarget = view;
			return true;
		} else {
			return super.dispatchTouchEvent(ev);
		}
	}
}
