package com.itheima.mobileguard.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.itheima.mobileguard.R;

public abstract class BaseLostSetupActivity extends Activity {

    /**
     * 手势探测器
     */
    private GestureDetector gd;
    protected SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        gd = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            public boolean onFling(MotionEvent e1, MotionEvent e2,
                                   float velocityX, float velocityY) {
                //水平方向在一定时间内变化了 才算正确的手势
                //e1  代表手指第一次触摸屏幕的事件
                //e2 代表手指离开屏幕一瞬间的事件
                //velocityX 水平方向的速度 单位  pix/s
                //velocityY 竖直方向的速度
                if (Math.abs(velocityX) < 200) {//速率小于两百
                    Toast.makeText(BaseLostSetupActivity.this, "无效的手势,太慢了", Toast.LENGTH_LONG).show();
                    return true;
                } else if ((e2.getRawX() - e1.getRawX()) > 200) {//手势向右 屏幕要向左
                    showPrevious();
                    overridePendingTransition(R.anim.previous_in_translate, R.anim.previous_out_translate);
                    return true;
                } else if ((e1.getRawX() - e2.getRawX()) > 200) {//手势向左 屏幕向右
                    showNext();
                    overridePendingTransition(R.anim.next_in_translate, R.anim.next_out_translate);
                    return true;//返回true 表示事件处理完毕
                }
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
    }

    /**
     * 3.用手势识别器去识别事件
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gd.onTouchEvent(event);//手势识别器 分析事件
        return super.onTouchEvent(event);
    }

    /**
     * 返回上一个设置界面
     *
     * @param v
     */
    public void previous(View v) {
        showPrevious();
        overridePendingTransition(R.anim.previous_in_translate, R.anim.previous_out_translate);
    }

    public abstract void showPrevious();

    /**
     * 点击事件 进入下一个页面
     *
     * @param view
     */
    public void next(View view) {
        showNext();
        overridePendingTransition(R.anim.next_in_translate, R.anim.next_out_translate);
    }

    /**
     * 由子类实现具体到哪个Activity
     */
    public abstract void showNext();

    /**
     * 开启新的Activity 并结束自己
     *
     * @param clazz
     */
    public void startActivityAndFinishSelf(Class<?> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
        finish();
    }
}
