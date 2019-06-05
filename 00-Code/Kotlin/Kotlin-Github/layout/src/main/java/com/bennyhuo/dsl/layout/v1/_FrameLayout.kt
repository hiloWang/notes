package com.bennyhuo.dsl.layout.v1

import android.content.Context
import android.view.View
import android.widget.FrameLayout

class _FrameLayout(context: Context): FrameLayout(context), DslViewParent<FrameLayout.LayoutParams>{

    var <T: View> T.layoutGravity: Int
        set(value){
            lparams.gravity = value
        }
        get(){
            return lparams.gravity
        }

    val <T: View> T.x1024: Int
        get() = 1
}