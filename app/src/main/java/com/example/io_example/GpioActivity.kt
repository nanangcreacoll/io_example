package com.example.io_example

import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.io_example.controller.Gpio

class GpioActivity : ComponentActivity() {
    private val whiteLedPin = 11 // GPIO0_B3
    private val greenLedPin = 1 // GPIO0_A1
    private val redLedPin = 13 // GPIO0_B5

    private lateinit var whiteLedButton: Button
    private lateinit var greenLedButton: Button
    private lateinit var redLedButton: Button

    private val whiteLed: Gpio = Gpio(whiteLedPin)
    private val greenLed: Gpio = Gpio(greenLedPin)
    private val redLed: Gpio = Gpio(redLedPin)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gpio)

        whiteLedButton = findViewById(R.id.whiteLedButton)
        greenLedButton = findViewById(R.id.greenLedButton)
        redLedButton = findViewById(R.id.redLedButton)

        whiteLedButton.setOnClickListener {
            toggleLed(whiteLed)
        }
        greenLedButton.setOnClickListener {
            toggleLed(greenLed)
        }
        redLedButton.setOnClickListener {
            toggleLed(redLed)
        }
    }

    private fun toggleLed(led: Gpio) {
        if (led.isOn) {
            led.off()
        } else {
            led.on()
        }
    }
}