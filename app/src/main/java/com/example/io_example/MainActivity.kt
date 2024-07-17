package com.example.io_example

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val gpioButton: Button = findViewById(R.id.gpio_button)
        val i2cButton: Button = findViewById(R.id.i2c_button)
        val serialButton: Button = findViewById(R.id.serial_button)

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