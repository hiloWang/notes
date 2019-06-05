package com.bennyhuo.dsl.layout.v2

import android.app.Activity
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout

//region ViewGroup
inline fun <reified T : ViewGroup> T.relativeLayout(init:(@DslViewMarker  _RelativeLayout).() -> Unit) =
        _RelativeLayout(context).also(this::addView).also(init)


inline fun <reified T : ViewGroup> T.linearLayout(init:(@DslViewMarker  _LinearLayout).() -> Unit) =
        _LinearLayout(context).also(this::addView).also(init)


inline fun <reified T : ViewGroup> T.frameLayout(init:(@DslViewMarker  _FrameLayout).() -> Unit) =
        _FrameLayout(context).also(this::addView).also(init)


inline fun <reified T : ViewGroup> T.verticalLayout(init:(@DslViewMarker  _LinearLayout).() -> Unit) =
        _LinearLayout(context)
                .also(this::addView)
                .apply {
                    orientation = LinearLayout.VERTICAL
                    init()
                }

inline fun <reified T : ViewGroup> T.button(init:(@DslViewMarker  Button).() -> Unit) =
        Button(context).also(this::addView).also(init)


inline fun <reified T : ViewGroup> T.textView(init:(@DslViewMarker  Button).() -> Unit) =
        Button(context).also(this::addView).also(init)


inline fun <reified T : ViewGroup> T.imageView(init:(@DslViewMarker  Button).() -> Unit) =
        Button(context).also(this::addView).also(init)

//endregion

//region Activity
inline fun <reified T : Activity> T.relativeLayout(init:(@DslViewMarker  _RelativeLayout).() -> Unit) =
        _RelativeLayout(this).also(this::setContentView).also(init)


inline fun <reified T : Activity> T.linearLayout(init:(@DslViewMarker  _LinearLayout).() -> Unit) =
        _LinearLayout(this).also(this::setContentView).also(init)


inline fun <reified T : Activity> T.frameLayout(init:(@DslViewMarker  _FrameLayout).() -> Unit) =
        _FrameLayout(this).also(this::setContentView).also(init)


inline fun <reified T : Activity> T.verticalLayout(init:(@DslViewMarker  _LinearLayout).() -> Unit) =
        _LinearLayout(this)
                .also(this::setContentView)
                .apply {
                    orientation = LinearLayout.VERTICAL
                    init()
                }

inline fun <reified T : Activity> T.button(init:(@DslViewMarker  Button).() -> Unit) =
        Button(this).also(this::setContentView).also(init)


inline fun <reified T : Activity> T.textView(init:(@DslViewMarker  Button).() -> Unit) =
        Button(this).also(this::setContentView).also(init)


inline fun <reified T : Activity> T.imageView(init:(@DslViewMarker  Button).() -> Unit) =
        Button(this).also(this::setContentView).also(init)

//endregion