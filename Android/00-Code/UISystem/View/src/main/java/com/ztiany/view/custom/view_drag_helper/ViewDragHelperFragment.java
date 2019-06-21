package com.ztiany.view.custom.view_drag_helper;

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

import com.ztiany.view.R;

/**
 * http://blog.csdn.net/lmj623565791/article/details/46858663
 *
 * @author Ztiany
 *         Email: ztiany3@gmail.com
 *         Date : 2017-10-13 19:07
 */
public class ViewDragHelperFragment extends Fragment {

    private FrameLayout mFrameLayout;

    private MenuItem.OnMenuItemClickListener mMenuItemClickListener = new MenuItem.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            int layoutId = 0;
            String title = item.getTitle().toString();
            switch (title) {
                case "sample":
                    layoutId = R.layout.view_drag_helper_slide_sample;
                    break;
                case "menu":
                    layoutId = R.layout.view_drag_helper_slide_menu;
                    break;
            }
            mFrameLayout.removeAllViews();
            LayoutInflater.from(getContext()).inflate(layoutId, mFrameLayout);
            return true;
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add("sample").setOnMenuItemClickListener(mMenuItemClickListener);
        menu.add("menu").setOnMenuItemClickListener(mMenuItemClickListener);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mFrameLayout == null) {
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            mFrameLayout = new FrameLayout(getContext());
            mFrameLayout.setLayoutParams(lp);
            LayoutInflater.from(getContext()).inflate(R.layout.view_drag_helper_slide_sample, mFrameLayout);
        }
        return mFrameLayout;
    }

}
