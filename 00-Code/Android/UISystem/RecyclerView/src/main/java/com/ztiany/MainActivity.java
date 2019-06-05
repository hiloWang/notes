package com.ztiany;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.ztiany.adapter_list.ListFragment;
import com.ztiany.adapter_list.RecyclerFragment;
import com.ztiany.diff_util.AsyncListDifferFragment;
import com.ztiany.diff_util.DiffUtilFragment;
import com.ztiany.item_decoraion_index.ItemDecorationIndexFragment;
import com.ztiany.itemtouch.GridFragment;
import com.ztiany.itemtouch.ItemTouchListenerFragment;
import com.ztiany.itemtouch.LinearFragment;
import com.ztiany.layoutmanager.CustomLayoutManagerFragment;
import com.ztiany.recyclerview.R;
import com.ztiany.snap.SnapHelperFragment;
import com.ztiany.swipe_menu.SwipeMenu1Fragment;
import com.ztiany.wrapcontent.WithScrollVIewFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private static final List<Item> LIST = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BaseUtils.init(getApplication());
        setContentView(R.layout.common_activity_main);
        initView();
    }

    private void initView() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    supportFinishAfterTransition();
                }
            });
        }

        mRecyclerView = findViewById(R.id.activity_main);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        setAdapter();
    }

    private void setAdapter() {
        mRecyclerView.setAdapter(new ItemAdapter(this, LIST));
    }

    static {
        LIST.add(new Item("Wrap RecyclerView", WithScrollVIewFragment.class));
        LIST.add(new Item("ItemDecoration实现分组索引", ItemDecorationIndexFragment.class));
        LIST.add(new Item("ItemTouchListener研究", ItemTouchListenerFragment.class));
        LIST.add(new Item("ItemTouch Linear", LinearFragment.class));
        LIST.add(new Item("ItemTouch Grid", GridFragment.class));
        LIST.add(new Item("Pager Snap Helper", SnapHelperFragment.class));
        LIST.add(new Item("ScrollView实现SwipeMenu", SwipeMenu1Fragment.class));
        LIST.add(new Item("自定义LinearLayoutManager", CustomLayoutManagerFragment.class));
        LIST.add(new Item("DiffUtil示例", DiffUtilFragment.class));
        LIST.add(new Item("AsyncListDiffer示例", AsyncListDifferFragment.class));
        LIST.add(new Item("Adapter对比 ListView", ListFragment.class));
        LIST.add(new Item("Adapter对比 RecyclerView", RecyclerFragment.class));
    }
}
