package com.bennyhuo.coroutines.ui

import java.awt.BorderLayout
import java.awt.Dimension
import java.awt.event.ActionEvent
import javax.swing.ImageIcon
import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel

class MainWindow: JFrame(){

    lateinit var button: JButton

    fun init(){
        button = JButton("Click me")
        contentPane.add(button, BorderLayout.NORTH)
    }

    fun onButtonClick(listener: (ActionEvent)->Unit){
        button.addActionListener(listener)
    }
}