package com.example.io_example

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.io_example.io.I2c

class I2cActivity : ComponentActivity() {
    private val i2cBus: Int = 4
    private val i2cAddress: Byte = 0x20
    private var value: Byte = 0x00

    private val i2c = I2c(i2cBus, i2cAddress)

    private lateinit var i2cWriteButton: Button
    private lateinit var i2cReadButton: Button
    private lateinit var i2cValue: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_i2c)

        i2cWriteButton = findViewById(R.id.i2cWriteButton)
        i2cReadButton = findViewById(R.id.i2cReadButton)
        i2cValue = findViewById(R.id.i2cValueView)

        i2cWriteButton.setOnClickListener {
            if (value < 0xff) {
                i2c.write(value)
                value++
            } else {
                value = 0x00
            }
        }

        i2cReadButton.setOnClickListener {
            val readValue = i2c.read()
            i2cValue.text = readValue
        }
    }
}