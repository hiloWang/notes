package com.ztiany.navigation.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.Navigation
import com.ztiany.navigation.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun supportNavigateUpTo(upIntent: Intent) {
        Navigation.findNavController(this, R.id.home_nav_host_fragment).navigateUp()
    }

}
