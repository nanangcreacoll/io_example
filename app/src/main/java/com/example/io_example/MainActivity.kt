package com.example.io_example

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    private lateinit var gpioButton: Button
    private lateinit var i2cButton: Button
    private lateinit var serialButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        gpioButton = findViewById(R.id.gpioButton)
        i2cButton = findViewById(R.id.i2cButton)
        serialButton = findViewById(R.id.serialButton)

        gpioButton.setOnClickListener {
            val intent = Intent(this, GpioActivity::class.java)
            startActivity(intent)
        }

        i2cButton.setOnClickListener {
            val intent = Intent(this, I2cActivity::class.java)
            startActivity(intent)
        }

        serialButton.setOnClickListener {
            val intent = Intent(this, SerialActivity::class.java)
            startActivity(intent)
        }
    }
}