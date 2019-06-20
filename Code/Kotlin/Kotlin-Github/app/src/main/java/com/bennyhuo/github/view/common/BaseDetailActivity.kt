package com.bennyhuo.github.view.common

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.GestureDetector
import android.view.MenuItem
import android.view.MotionEvent
import com.bennyhuo.github.R
import com.bennyhuo.github.view.config.Themer
import com.bennyhuo.swipefinishable.SwipeFinishable
import com.bennyhuo.swipefinishable.SwipeFinishable.SwipeFinishableActivity
import com.bennyhuo.tieguanyin.annotations.ActivityBuilder
import com.bennyhuo.tieguanyin.annotations.PendingTransition
import org.jetbrains.anko.dip

@ActivityBuilder(pendingTransition = PendingTransition(enterAnim = R.anim.rignt_in, exitAnim = R.anim.left_out))
abstract class BaseDetailActivity: AppCompatActivity() {

    private val swipeBackTouchDelegate by lazy { SwipeBackTouchDelegate(this, ::finish) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Themer.applyProperTheme(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home ->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item);
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.left_in, R.anim.rignt_out)
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        return  swipeBackTouchDelegate.onTouchEvent(ev) || super.dispatchTouchEvent(ev)
    }
}

class SwipeBackTouchDelegate(val activity: Activity, block: ()-> Unit){
    companion object {
        private const val MIN_FLING_TO_BACK = 2000
    }

    private val minFlingToBack by lazy{
        activity.dip(MIN_FLING_TO_BACK)
    }

    private val swipeBackDelegate by lazy {
        GestureDetector(activity, object: GestureDetector.SimpleOnGestureListener(){
            override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
                return if(velocityX > minFlingToBack){
                    block()
                    true
                } else {
                    false
                }
            }
        })
    }

    fun onTouchEvent(event: MotionEvent) = swipeBackDelegate.onTouchEvent(event)
}

@ActivityBuilder(pendingTransition = PendingTransition(enterAnim = 0, exitAnim = 0))
abstract class BaseDetailSwipeFinishableActivity: AppCompatActivity(), SwipeFinishableActivity{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Themer.applyProperTheme(this, true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
        // Respond to the action bar's Up/Home button
            android.R.id.home ->{
                finish()
                return true
            }
        }
        return super.onOptionsItemSelected(item);
    }

    override fun finishThisActivity() {
        super.finish()
    }

    override fun finish() {
        SwipeFinishable.INSTANCE.finishCurrentActivity()
    }
}
