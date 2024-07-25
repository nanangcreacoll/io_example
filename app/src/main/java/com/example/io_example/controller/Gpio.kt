package com.example.io_example.controller

import android.util.Log
import java.io.File

class Gpio(private val pin: Int, direction: String = "out"): Command() {
    private val pinPath: String = "/sys/class/gpio/gpio$pin"
    private val directionPath: String = "$pinPath/direction"
    private val valuePath: String = "$pinPath/value"

    var isOn: Boolean = false

    init {
        if (File(directionPath).exists()) {
            setDirection(direction)
        }
    }

    private fun setDirection(direction: String) {
        executeRootCommand("echo", "$direction > $directionPath")
        Log.d("Gpio", "Gpio$pin direction set to $direction")
    }

    private fun setValue(value: String) {
        executeRootCommand("echo","$value > $valuePath")
    }

    fun on() {
        isOn = true
        setValue("1")
    }

    fun off() {
        isOn = false
        setValue("0")
    }
}