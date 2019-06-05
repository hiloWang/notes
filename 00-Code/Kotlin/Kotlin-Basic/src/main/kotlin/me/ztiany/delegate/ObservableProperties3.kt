package me.ztiany.delegate

import java.beans.PropertyChangeListener
import java.beans.PropertyChangeSupport

import kotlin.reflect.KProperty


class ObservableProperty3(var propValue: Int, val changeSupport: PropertyChangeSupport) {

    operator fun getValue(p: Person3, prop: KProperty<*>): Int = propValue

    operator fun setValue(p: Person3, prop: KProperty<*>, newValue: Int) {
        val oldValue = propValue
        propValue = newValue
        changeSupport.firePropertyChange(prop.name, oldValue, newValue)
    }
}

class Person3(val name: String, age: Int, salary: Int) : PropertyChangeAware() {
    var age: Int by ObservableProperty3(age, changeSupport)
    var salary: Int by ObservableProperty3(salary, changeSupport)
}

fun main(args: Array<String>) {
    val p = Person("Dmitry", 34, 2000)
    p.addPropertyChangeListener(
            PropertyChangeListener { event ->
                println("Property ${event.propertyName} changed " +
                        "from ${event.oldValue} to ${event.newValue}")
            }
    )
    p.age = 35
    p.salary = 2100
}
