package com.bennyhuo.github.utils

import com.bennyhuo.github.AppContext
import org.jetbrains.anko.connectivityManager

object Network {
    fun isAvailable(): Boolean = AppContext.connectivityManager.activeNetworkInfo?.isAvailable ?: false
}