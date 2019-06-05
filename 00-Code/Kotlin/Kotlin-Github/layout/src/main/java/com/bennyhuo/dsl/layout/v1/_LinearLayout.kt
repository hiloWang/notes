package com.bennyhuo.dsl.layout.v1

import android.content.Context
import android.view.View
import android.widget.LinearLayout

const val MATCH_PARENT = -1
const val WRAP_CONTENT = -2

class _LinearLayout(context: Context): LinearLayout(context), DslViewParent<LinearLayout.LayoutParams> {

    var <T: View> T.layoutWeight: Float
        set(value){
            lparams.weight = value
        }
        get(){
            return lparams.weight
        }

    var <T: View> T.layoutGravity: Int
        set(value){
            lparams.gravity = value
        }
        get(){
            return lparams.gravity
        }
}