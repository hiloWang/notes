package com.bennyhuo.dsl.layout.v1

import android.annotation.TargetApi
import android.content.Context
import android.os.Build.VERSION_CODES
import android.view.View
import android.widget.RelativeLayout

/**
 * Created by benny on 02/04/2018.
 */
open class _RelativeLayout(context: Context) : RelativeLayout(context), DslViewParent<RelativeLayout.LayoutParams> {

    fun <T : View> T.leftOf(id: Int) {
        lparams.addRule(LEFT_OF, id)
    }

    fun <T : View> T.rightOf(id: Int) {
        lparams.addRule(RIGHT_OF, id)
    }

    fun <T : View> T.above(id: Int) {
        lparams.addRule(ABOVE, id)
    }

    fun <T : View> T.below(id: Int) {
        lparams.addRule(BELOW, id)
    }

    fun <T : View> T.alignBaseline(id: Int) {
        lparams.addRule(ALIGN_BASELINE, id)
    }

    fun <T : View> T.alignLeft(id: Int) {
        lparams.addRule(ALIGN_LEFT, id)
    }

    fun <T : View> T.alignTop(id: Int) {
        lparams.addRule(ALIGN_TOP, id)
    }

    fun <T : View> T.alignRight(id: Int) {
        lparams.addRule(ALIGN_RIGHT, id)
    }

    fun <T : View> T.alignBottom(id: Int) {
        lparams.addRule(ALIGN_BOTTOM, id)
    }

    fun <T : View> T.alignParentLeft() {
        lparams.addRule(ALIGN_PARENT_LEFT)
    }

    fun <T : View> T.alignParentTop() {
        lparams.addRule(ALIGN_PARENT_TOP)
    }

    fun <T : View> T.alignParentRight() {
        lparams.addRule(ALIGN_PARENT_RIGHT)
    }

    fun <T : View> T.alignParentBottom() {
        lparams.addRule(ALIGN_PARENT_BOTTOM)
    }

    fun <T : View> T.centerInParent() {
        lparams.addRule(CENTER_IN_PARENT)
    }

    fun <T : View> T.centerHorizontal() {
        lparams.addRule(CENTER_HORIZONTAL)
    }

    fun <T : View> T.centerVertical() {
        lparams.addRule(CENTER_VERTICAL)
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    fun <T : View> T.startOf(id: Int) {
        lparams.addRule(START_OF, id)
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    fun <T : View> T.endOf(id: Int) {
        lparams.addRule(END_OF, id)
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    fun <T : View> T.alignStart(id: Int) {
        lparams.addRule(ALIGN_START, id)
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    fun <T : View> T.alignEnd(id: Int) {
        lparams.addRule(ALIGN_END, id)
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    fun <T : View> T.alignParentStart() {
        lparams.addRule(ALIGN_PARENT_START)
    }

    @TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    fun <T : View> T.alignParentEnd() {
        lparams.addRule(ALIGN_PARENT_END)
    }
}