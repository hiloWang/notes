package com.ztiany.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.ztiany.view.animation.circular_reveal.CircularRevealActivity;
import com.ztiany.view.animation.reversal.ReversalActivity;
import com.ztiany.view.animation.spring.SpringScrollViewFragment;
import com.ztiany.view.constraint_layout.ConstraintLayoutActivity;
import com.ztiany.view.custom.CustomViewFragment;
import com.ztiany.view.custom.flow_layout.FlowLayoutFragment;
import com.ztiany.view.custom.message_drag.MessageDragFragment;
import com.ztiany.view.custom.pull_refresh.PullToRefreshFragment;
import com.ztiany.view.dialogs.DialogsActivity;
import com.ztiany.view.draw.CanvasFragment;
import com.ztiany.view.draw.MatrixFragment;
import com.ztiany.view.draw.PathFragment;
import com.ztiany.view.draw.camera.Camera3DFragment;
import com.ztiany.view.draw.camera.Camera3DTheoryFragment;
import com.ztiany.view.draw.camera.CameraDemoViewFragment;
import com.ztiany.view.draw.color.ColorMatrixFilterFragment;
import com.ztiany.view.draw.color.MaskFilterFragment;
import com.ztiany.view.drawable.DrawableBitmapFragment;
import com.ztiany.view.drawable.DrawableLayerFragment;
import com.ztiany.view.drawable.DrawableRotateFragment;
import com.ztiany.view.drawable.DrawableSelectorFragment;
import com.ztiany.view.drawable.DrawableVectorFragment;
import com.ztiany.view.layout_inflater.LayoutInflaterActivity;
import com.ztiany.view.scroll.ScrollFragment;
import com.ztiany.view.scroll.sticky.StickyNavigationFragment;
import com.ztiany.view.custom.view_drag_helper.ViewDragHelperFragment;
import com.ztiany.view.custom.view_pager.ViewPagerFragment;
import com.ztiany.view.window.RealWindowSizeActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private static final List<Item> LIST = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity_main);
        initView();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setContentInsetStartWithNavigation(0);

        mRecyclerView = (RecyclerView) findViewById(R.id.activity_main);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        setAdapter();
    }

    private void setAdapter() {
        mRecyclerView.setAdapter(new ItemAdapter(this, LIST));
    }

    static {
        LIST.add(new Item("事件&滑动", ScrollFragment.class));
        LIST.add(new Item("Sticky Navigation", StickyNavigationFragment.class));
        LIST.add(new Item("Canvas绘制", CanvasFragment.class));
        LIST.add(new Item("矩阵", MatrixFragment.class));
        LIST.add(new Item("ColorMatrixFilter", ColorMatrixFilterFragment.class));
        LIST.add(new Item("MaskFilter", MaskFilterFragment.class));
        LIST.add(new Item("Path&贝塞尔曲线", PathFragment.class));
        LIST.add(new Item("Camera变换", CameraDemoViewFragment.class));
        LIST.add(new Item("Camera3D View", Camera3DFragment.class));
        LIST.add(new Item("Camera3D 原理", Camera3DTheoryFragment.class));
        LIST.add(new Item("ViewDragHelper使用", ViewDragHelperFragment.class));
        LIST.add(new Item("自定义View", CustomViewFragment.class));
        LIST.add(new Item("消息的拖拽", MessageDragFragment.class));
        LIST.add(new Item("流式布局", FlowLayoutFragment.class));
        LIST.add(new Item("下拉刷新", PullToRefreshFragment.class));

        LIST.add(new Item("Spring动画", SpringScrollViewFragment.class));
        LIST.add(new Item("CircularReveal动画", CircularRevealActivity.class));
        LIST.add(new Item("翻转动画", ReversalActivity.class));

        LIST.add(new Item("BitmapDrawable", DrawableBitmapFragment.class));
        LIST.add(new Item("LayerDrawable", DrawableLayerFragment.class));
        LIST.add(new Item("RotateDrawable", DrawableRotateFragment.class));
        LIST.add(new Item("SelectorDrawable", DrawableSelectorFragment.class));
        LIST.add(new Item("VectorDrawable", DrawableVectorFragment.class));

        LIST.add(new Item("Dialog相关", DialogsActivity.class));
        LIST.add(new Item("查看真实的Window Size", RealWindowSizeActivity.class));

        LIST.add(new Item("LayoutInflaterCompat", LayoutInflaterActivity.class));

        LIST.add(new Item("ConstraintLayout示例", ConstraintLayoutActivity.class));
        LIST.add(new Item("ViewPager", ViewPagerFragment.class));
    }
}
