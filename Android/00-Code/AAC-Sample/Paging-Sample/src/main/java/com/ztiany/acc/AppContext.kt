package com.ztiany.acc

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex

/**
 *
 *@author Ztiany
 *      Email: ztiany3@gmail.com
 *      Date : 2018-04-28 16:10
 */
class AppContext : Application() {

    companion object {
        private lateinit var INSTANCE: AppContext
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }

    fun getAppContext() = INSTANCE


}