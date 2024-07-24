package com.example.io_example.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.io_example.R
import com.example.io_example.controller.I2c

class I2cActivity : ComponentActivity() {
    private val i2cBus: Int = 4
    private val i2cAddress: Byte = 0x20
    private var value: Byte = 0x00

    private val i2c = I2c(i2cBus, i2cAddress)

    private lateinit var i2cWriteButton: Button
    private lateinit var i2cReadButton: Button
    private lateinit var i2cValue: TextView

    @OptIn(ExperimentalStdlibApi::class)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_i2c)

        i2cWriteButton = findViewById(R.id.i2cWriteButton)
        i2cReadButton = findViewById(R.id.i2cReadButton)
        i2cValue = findViewById(R.id.i2cValueView)

        i2cWriteButton.setOnClickListener {
            runOnUiThread {
                i2cValue.text = "Writing: 0x${value.toHexString()} ..."
            }

            if (value < 0xff) {
                i2c.write(value)
                runOnUiThread {
                    i2cValue.text = "Written: 0x${value.toHexString()}"
                }
                value++
            } else {
                value = 0x00
                runOnUiThread {
                    i2cValue.text = "Written: 0x${value.toHexString()}"
                }
            }
        }

        i2cReadButton.setOnClickListener {
            runOnUiThread {
                i2cValue.text = "Reading..."
            }
            val readValue = i2c.read()
            runOnUiThread {
                i2cValue.text = readValue
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}