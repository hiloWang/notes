package com.mwqi.ui.holder;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.mwqi.R;
import com.mwqi.http.image.ImageLoader;
import com.mwqi.ui.widget.IndicatorView;
import com.mwqi.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mwqi on 2014/6/7.
 */
public class HomePictureHolder extends BaseHolder<List<String>> implements OnPageChangeListener {

    private RelativeLayout mHeader;
    private ViewPager mViewPager;
    private MyPagerAdapter mPagerAdapter;
    private IndicatorView mIndicator;
    private AutoPlayRunnable mAutoPlayRunnable;

    @Override
    protected View initView() {
        mHeader = new RelativeLayout(UIUtils.getContext());
        AbsListView.LayoutParams al = new AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, UIUtils.getDimens(R.dimen.list_header_hight));
        mHeader.setLayoutParams(al);

        mViewPager = new ViewPager(UIUtils.getContext());
        RelativeLayout.LayoutParams rl = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mViewPager.setLayoutParams(rl);
        mPagerAdapter = new MyPagerAdapter();
        mViewPager.setAdapter(mPagerAdapter);

        mIndicator = new IndicatorView(UIUtils.getContext());
        //设置点和点之间的间隙
        mIndicator.setInterval(5);
        //设置点的图片
        mIndicator.setIndicatorDrawable(UIUtils.getDrawable(R.drawable.indicator));
        rl = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rl.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        rl.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        rl.setMargins(0, 0, 20, 20);
        mIndicator.setLayoutParams(rl);
        mIndicator.setSelection(0);

        mViewPager.setOnPageChangeListener(this);

        mHeader.addView(mViewPager);
        mHeader.addView(mIndicator);

        mAutoPlayRunnable = new AutoPlayRunnable();
        mViewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    mAutoPlayRunnable.stop();
                } else if (event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL) {
                    mAutoPlayRunnable.start();
                }
                return false;
            }
        });

        return mHeader;
    }

    @Override
    public void refreshView() {
        List<String> datas = getData();
        mIndicator.setCount(datas.size());
        mViewPager.setCurrentItem(datas.size() * 1000, false);
        mAutoPlayRunnable.start();
    }

    class MyPagerAdapter extends PagerAdapter {

        List<ImageView> mViewCache = new ArrayList<ImageView>();

        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean isViewFromObject(View view, Object o) {
            return view == o;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (object != null && object instanceof ImageView) {
                ImageView imageView = (ImageView) object;
                container.removeView(imageView);
                mViewCache.add(imageView);
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView;
            if (mViewCache.size() > 0) {
                imageView = mViewCache.remove(0);
            } else {
                imageView = new ImageView(UIUtils.getContext());
                imageView.setScaleType(ScaleType.FIT_XY);
            }
            int index = position % (getData().size());
            String url = getData().get(index);
            ImageLoader.load(imageView, url);

            container.addView(imageView, 0);
            return imageView;
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int positon) {
        mIndicator.setSelection(positon % getData().size());
    }

    private class AutoPlayRunnable implements Runnable {

        private int AUTO_PLAY_INTERVAL = 5000;
        private boolean mShouldAutoPlay;

        public AutoPlayRunnable() {
            mShouldAutoPlay = false;
        }

        public void start() {
            if (!mShouldAutoPlay) {
                mShouldAutoPlay = true;
                UIUtils.removeCallbacks(this);
                UIUtils.postDelayed(this, AUTO_PLAY_INTERVAL);
            }
        }

        public void stop() {
            if (mShouldAutoPlay) {
                UIUtils.removeCallbacks(this);
                mShouldAutoPlay = false;
            }
        }

        @Override
        public void run() {
            if (mShouldAutoPlay) {
                UIUtils.removeCallbacks(this);
                int position = mViewPager.getCurrentItem();
                mViewPager.setCurrentItem(position + 1, true);
                UIUtils.postDelayed(this, AUTO_PLAY_INTERVAL);
            }
        }
    }
}
