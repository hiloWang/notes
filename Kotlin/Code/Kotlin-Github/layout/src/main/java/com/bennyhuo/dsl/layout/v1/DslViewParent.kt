package com.bennyhuo.dsl.layout.v1

import android.annotation.TargetApi
import android.os.Build.VERSION_CODES
import android.view.View
import android.view.ViewGroup
import kotlin.annotation.AnnotationTarget.*

@DslMarker
@Target(CLASS, TYPE, TYPEALIAS)
annotation class DslViewMarker

@DslViewMarker
interface DslViewParent<out P : ViewGroup.MarginLayoutParams> {
    val <T : View> T.lparams: P
        get() = layoutParams as P

    var <T : View> T.leftMargin: Int
        set(value) {
            lparams.leftMargin = value
        }
        get() {
            return lparams.leftMargin
        }

    var <T : View> T.topMargin: Int
        set(value) {
            lparams.topMargin = value
        }
        get() {
            return lparams.topMargin
        }

    var <T : View> T.rightMargin: Int
        set(value) {
            lparams.rightMargin = value
        }
        get() {
            return lparams.rightMargin
        }

    var <T : View> T.bottomMargin: Int
        set(value) {
            lparams.bottomMargin = value
        }
        get() {
            return lparams.bottomMargin
        }

    @get:TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    @set:TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    var <T : View> T.startMargin: Int
        set(value) {
            lparams.marginStart = value
        }
        get() {
            return lparams.marginStart
        }

    @get:TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    @set:TargetApi(VERSION_CODES.JELLY_BEAN_MR1)
    var <T : View> T.endMargin: Int
        set(value) {
            lparams.marginEnd = value
        }
        get() {
            return lparams.marginEnd
        }

    fun <T : View> T.margin(margin: Int) {
        leftMargin = margin
        topMargin = margin
        rightMargin = margin
        bottomMargin = margin
        startMargin = margin
        endMargin = margin
    }

    var <T : View> T.layoutWidth: Int
        set(value) {
            lparams.width = value
        }
        get() {
            return lparams.width
        }

    var <T : View> T.layoutHeight: Int
        set(value) {
            lparams.height = value
        }
        get() {
            return lparams.height
        }

}
