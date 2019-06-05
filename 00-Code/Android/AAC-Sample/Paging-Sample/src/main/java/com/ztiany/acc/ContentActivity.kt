package com.ztiany.acc

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.ztiany.acc.live.LiveDataFragment
import com.ztiany.acc.rx.RxJavaFragment

class ContentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content)
        val type = intent.getStringExtra(MainActivity.TYPE)
        if (type == MainActivity.LIVE_DATA) {
            showFragment(LiveDataFragment())
        } else if (type == MainActivity.RXJAVA) {
            showFragment(RxJavaFragment())
        }
    }

    private fun showFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.fl_content, fragment)
                .commit()
    }

}
