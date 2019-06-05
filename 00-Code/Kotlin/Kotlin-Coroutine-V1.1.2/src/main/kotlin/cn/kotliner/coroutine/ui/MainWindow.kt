package cn.kotliner.coroutine.ui

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.*


/**
 * Created by benny on 5/20/17.
 */
class MainWindow : JFrame() {

    private lateinit var button: JButton
    private lateinit var image: JLabel

    fun init() {
        button = JButton("点我获取慕课网Logo")
        image = JLabel()
        image.size = Dimension(200, 80)

        contentPane.add(button, BorderLayout.NORTH)
        contentPane.add(image, BorderLayout.CENTER)
    }

    fun onButtonClick(listener: (ActionEvent) -> Unit) {
        button.addActionListener(listener)
    }

    fun setLogo(logoData: ByteArray) {
        image.icon = ImageIcon(logoData)
    }
}