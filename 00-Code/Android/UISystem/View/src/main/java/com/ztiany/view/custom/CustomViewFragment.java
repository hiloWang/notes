package com.ztiany.view.custom;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.ztiany.view.custom.ruler.RulerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-08-05 15:44
 */
public class CustomViewFragment extends Fragment {


    private List<View> mViewList = new ArrayList<>();
    private FrameLayout mFrameLayout;

    private String[] titles = {
            "水平滑动",
            "BitmapDecor显式大图",
            "Surface Loading",
            "宫格锁",
            "圆环",
            "尺子",
            "正方形布局",
            "可缩放的ImageView",
            "SurfaceViewSinFun"
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewList.add(HScrollLayoutBuilder.buildHScrollLayout(getContext()));
        mViewList.add(new LargeImageView(getContext()));
        mViewList.add(new LoadingView(getContext()));
        mViewList.add(new LockPatternView(getContext()));
        mViewList.add(new Ring(getContext(), null));
        mViewList.add(new RulerView(getContext()));
        mViewList.add(new SquareEnhanceLayout(getContext()));
        mViewList.add(new ZoomImageView(getContext()));
        mViewList.add(new SurfaceViewSinFun(getContext()));
        setHasOptionsMenu(true);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return mFrameLayout != null ? mFrameLayout : (mFrameLayout = new FrameLayout(getContext()));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFrameLayout.removeAllViews();
        mFrameLayout.addView(mViewList.get(0));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem.OnMenuItemClickListener onMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                mFrameLayout.removeAllViews();
                mFrameLayout.addView(mViewList.get(item.getItemId()));
                return true;
            }
        };

        for (int i = 0; i < titles.length; i++) {
            menu.add(Menu.NONE, i, i, titles[i]).setOnMenuItemClickListener(onMenuItemClickListener);
        }
    }
}
