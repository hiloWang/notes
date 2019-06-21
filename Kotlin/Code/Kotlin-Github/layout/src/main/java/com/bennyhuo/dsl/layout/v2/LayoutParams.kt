package com.bennyhuo.dsl.layout.v2

import android.annotation.TargetApi
import android.os.Build.VERSION_CODES
import android.view.ViewGroup.MarginLayoutParams
import android.widget.RelativeLayout

//region MarginLayoutParam
@get:TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
@set:TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
var <T : MarginLayoutParams> T.startMargin: Int
    set(value) {
        marginStart = value
    }
    get() {
        return marginStart
    }

@get:TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
@set:TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
var <T : MarginLayoutParams> T.endMargin: Int
    set(value) {
        marginEnd = value
    }
    get() {
        return marginEnd
    }

fun <T : MarginLayoutParams> T.margin(margin: Int) {
    leftMargin = margin
    topMargin = margin
    rightMargin = margin
    bottomMargin = margin
    startMargin = margin
    endMargin = margin
}
//endregion

//region RelativeLayoutParam
fun <T : RelativeLayout.LayoutParams> T.leftOf(id: Int) {
    addRule(RelativeLayout.LEFT_OF, id)
}

fun <T : RelativeLayout.LayoutParams> T.rightOf(id: Int) {
    addRule(RelativeLayout.RIGHT_OF, id)
}

fun <T : RelativeLayout.LayoutParams> T.above(id: Int) {
    addRule(RelativeLayout.ABOVE, id)
}

fun <T : RelativeLayout.LayoutParams> T.below(id: Int) {
    addRule(RelativeLayout.BELOW, id)
}

fun <T : RelativeLayout.LayoutParams> T.alignBaseline(id: Int) {
    addRule(RelativeLayout.ALIGN_BASELINE, id)
}

fun <T : RelativeLayout.LayoutParams> T.alignLeft(id: Int) {
    addRule(RelativeLayout.ALIGN_LEFT, id)
}

fun <T : RelativeLayout.LayoutParams> T.alignTop(id: Int) {
    addRule(RelativeLayout.ALIGN_TOP, id)
}

fun <T : RelativeLayout.LayoutParams> T.alignRight(id: Int) {
    addRule(RelativeLayout.ALIGN_RIGHT, id)
}

fun <T : RelativeLayout.LayoutParams> T.alignBottom(id: Int) {
    addRule(RelativeLayout.ALIGN_BOTTOM, id)
}

fun <T : RelativeLayout.LayoutParams> T.alignParentLeft() {
    addRule(RelativeLayout.ALIGN_PARENT_LEFT)
}

fun <T : RelativeLayout.LayoutParams> T.alignParentTop() {
    addRule(RelativeLayout.ALIGN_PARENT_TOP)
}

fun <T : RelativeLayout.LayoutParams> T.alignParentRight() {
    addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
}

fun <T : RelativeLayout.LayoutParams> T.alignParentBottom() {
    addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
}

fun <T : RelativeLayout.LayoutParams> T.centerInParent() {
    addRule(RelativeLayout.CENTER_IN_PARENT)
}

fun <T : RelativeLayout.LayoutParams> T.centerHorizontal() {
    addRule(RelativeLayout.CENTER_HORIZONTAL)
}

fun <T : RelativeLayout.LayoutParams> T.centerVertical() {
    addRule(RelativeLayout.CENTER_VERTICAL)
}

@TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
fun <T : RelativeLayout.LayoutParams> T.startOf(id: Int) {
    addRule(RelativeLayout.START_OF, id)
}

@TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
fun <T : RelativeLayout.LayoutParams> T.endOf(id: Int) {
    addRule(RelativeLayout.END_OF, id)
}

@TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
fun <T : RelativeLayout.LayoutParams> T.alignStart(id: Int) {
    addRule(RelativeLayout.ALIGN_START, id)
}

@TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
fun <T : RelativeLayout.LayoutParams> T.alignEnd(id: Int) {
    addRule(RelativeLayout.ALIGN_END, id)
}

@TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
fun <T : RelativeLayout.LayoutParams> T.alignParentStart() {
    addRule(RelativeLayout.ALIGN_PARENT_START)
}

@TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
fun <T : RelativeLayout.LayoutParams> T.alignParentEnd() {
    addRule(RelativeLayout.ALIGN_PARENT_END)
}
//endregion
