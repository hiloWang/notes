package com.bennyhuo.coroutines.sample

import com.bennyhuo.coroutines.ui.MainWindow
import com.bennyhuo.coroutines.utils.log
import kotlinx.coroutines.experimental.*
import kotlinx.coroutines.experimental.swing.Swing
import javax.swing.JFrame

fun main(args: Array<String>) {
    val frame = MainWindow()
    frame.title = "Coroutine@Bennyhuo"
    frame.setSize(600, 100)
    frame.isResizable = true
    frame.defaultCloseOperation = JFrame.EXIT_ON_CLOSE
    frame.init()
    frame.isVisible = true

    //addActionListener1(frame)
    addActionListener2(frame)
}

private fun addActionListener1(frame: MainWindow) {
    frame.button.addActionListener {
        launch(Swing) {
            log(-1)
            val job = launch {
                log(1)
                delay(1000L)
                log(2)
            }
            log(-2)
            job.join()
            log(-3)
        }
    }
}

private fun addActionListener2(frame: MainWindow) {
    frame.button.addActionListener {
        log(-1)
        launch(Swing) {
            log(-3)
            val result = async {
                delay(1000L)
                1
            }
            log(-4)
            log("result = ${result.await()}")
            log(-5)
        }
        log(-2)
    }
}
