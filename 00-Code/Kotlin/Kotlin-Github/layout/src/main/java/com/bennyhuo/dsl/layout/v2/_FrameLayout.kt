package com.bennyhuo.dsl.layout.v2

import android.content.Context
import android.view.View
import android.widget.FrameLayout

class _FrameLayout(context: Context): FrameLayout(context), DslViewParent{
    fun <T: View> T.lparams(width: Int = WRAP_CONTENT, height: Int = WRAP_CONTENT, init: LayoutParams.()->Unit){
        this.layoutParams = LayoutParams(width, height).also(init)
    }
}