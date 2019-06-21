package com.ztiany.acc

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        const val LIVE_DATA = "LiveData"
        const val RXJAVA = "RxJava"
        const val TYPE = "TYPE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupView()
    }

    private fun setupView() {
        btnLiveData.setOnClickListener {
            val intent = Intent(this, ContentActivity::class.java)
            intent.putExtra(TYPE, LIVE_DATA)
            startActivity(intent)
        }

        btnRxJava.setOnClickListener {
            val intent = Intent(this, ContentActivity::class.java)
            intent.putExtra(TYPE, RXJAVA)
            startActivity(intent)
        }

    }

}
