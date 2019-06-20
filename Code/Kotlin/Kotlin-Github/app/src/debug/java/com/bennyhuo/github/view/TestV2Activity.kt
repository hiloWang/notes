package com.bennyhuo.github.view

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import com.bennyhuo.dsl.layout.v2.MATCH_PARENT
import com.bennyhuo.dsl.layout.v2.button
import com.bennyhuo.dsl.layout.v2.frameLayout
import com.bennyhuo.dsl.layout.v2.verticalLayout

class TestV2Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        frameLayout {
            setBackgroundColor(Color.RED)

            verticalLayout {
                button {
                    text = "Button 1"
                    setBackgroundColor(Color.YELLOW)
                }.lparams {
                    weight = 1f
                }

                button {
                    text = "Button 2"
                    setBackgroundColor(Color.GREEN)
                }.lparams {
                    weight = 3f
                }
            }.lparams(height = MATCH_PARENT) {
                gravity = Gravity.RIGHT
            }
        }
    }
}