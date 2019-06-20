package com.bennyhuo.dsl.layout.v2

import android.content.Context
import android.view.View
import android.widget.RelativeLayout

open class _RelativeLayout(context: Context) : RelativeLayout(context), DslViewParent {

    fun <T: View> T.lparams(width: Int = WRAP_CONTENT, height: Int = WRAP_CONTENT, init: LayoutParams.()->Unit){
        this.layoutParams = LayoutParams(width, height).also(init)
    }

}