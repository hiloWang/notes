package ztiany.chapter2

import javax.swing.JButton
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.JOptionPane
import javax.swing.WindowConstants
import java.awt.FlowLayout
import java.awt.event.ActionListener
import java.awt.event.FocusListener
import java.awt.event.MouseListener
import java.awt.event.MouseMotionListener

//如果应用要求的行为是动态的，而且只有在运行时才知道接口的名字，有该如何呢？asType方法可以帮忙
//通过将预实现的接口的Class元对象作为一个参数发送给asType，可以把代码或者映射转化为接口

frame = new JFrame(size: [300, 300], layout: new FlowLayout(), defaultCloseOperation: WindowConstants.EXIT_ON_CLOSE)

button = new JButton("Click")
positionLabel = new JLabel("")
msgLabel = new JLabel("")
frame.contentPane.add button
frame.contentPane.add positionLabel
frame.contentPane.add msgLabel

button.addActionListener(
        {
            JOptionPane.showMessageDialog(frame, "you clicked!")
        } as ActionListener
)
displayMouseLcation = {
    positionLabel.setText("$it.x, $it.y")
}

frame.addMouseListener(displayMouseLcation as MouseListener)
frame.addMouseMotionListener(displayMouseLcation as MouseMotionListener)

handleFocus = [
        focusGained: {
            msgLabel.setText("good to see you!")
        },
        focusLost  : {
            msgLabel.setText("come back soon!")
        }
]

button.addFocusListener(handleFocus as FocusListener)

events = ['WindowListener', 'ComponentListener']
//events的列表可能是动态的，而且可能来自某些输入
handler = {
    msgLabel.setText "$it"
}

for (event in events) {
    handlerImpl = handler.asType(Class.forName("java.awt.event.${event}"))
    frame."add${event}"(handlerImpl)
}

frame.show()
//上面代码使用了asType方法，如果不同的方法有不同的实习，就是使用一个映射代替单个代码块












