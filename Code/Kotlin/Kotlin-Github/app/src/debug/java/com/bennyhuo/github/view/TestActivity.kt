package com.bennyhuo.github.view

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import com.bennyhuo.dsl.layout.v1.*

class TestActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        frameLayout {
            setBackgroundColor(Color.RED)

            verticalLayout {
                layoutHeight = MATCH_PARENT
                layoutWidth = WRAP_CONTENT

                layoutGravity = Gravity.RIGHT

                button {
                    text = "Button 1"
                    //layoutWeight = 1f // implicit receiver.
                    setBackgroundColor(Color.YELLOW)
                }

                button {
                    text = "Button 2"
                    //layoutWeight = 3f
                    setBackgroundColor(Color.GREEN)
                }
            }
        }
    }
}