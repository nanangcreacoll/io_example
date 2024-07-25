package com.example.io_example.activity

import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import com.example.io_example.controller.Bluetooth
import com.example.io_example.R

class BluetoothActivity : ComponentActivity() {
    private lateinit var bluetoothController: Bluetooth
    private lateinit var bluetoothStartButton: Button
    private lateinit var bluetoothDeviceView: TextView

    private val deviceValues: MutableMap<String, String> = mutableMapOf()

    // MAC address, service UUID, characteristic UUID, descriptor UUID
    private val bleList: List<List<String>> = listOf(
        listOf("17:71:12:4E:C6:95", "0000ffe0-0000-1000-8000-00805f9b34fb", "0000ffe4-0000-1000-8000-00805f9b34fb", "00002902-0000-1000-8000-00805f9b34fb"), // Pulse Oximeter
        listOf("64:FB:01:16:98:AE", "0000fff0-0000-1000-8000-00805f9b34fb", "0000fff1-0000-1000-8000-00805f9b34fb", "00002902-0000-1000-8000-00805f9b34fb"), // Weight Scale
        listOf("C0:30:00:31:D7:B8", "00001810-0000-1000-8000-00805f9b34fb", "00002a35-0000-1000-8000-00805f9b34fb", "00002902-0000-1000-8000-00805f9b34fb") // Blood Pressure
    )

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bluetooth)

        bluetoothStartButton = findViewById(R.id.bluetoothStartButton)
        bluetoothDeviceView = findViewById(R.id.bluetoothDeviceView)

        bluetoothController = Bluetooth(this, bleList) { address, value ->
            runOnUiThread {
                deviceValues[address] = value
                updateDeviceView()
            }
        }

        bluetoothStartButton.setOnClickListener {
            bluetoothController.startScanning()
        }
    }

    private fun updateDeviceView() {
        val displayText = deviceValues.entries.joinToString("\n") { (address, value) ->
            "Device: $address\nValue: $value\n"
        }
        bluetoothDeviceView.text = displayText
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onDestroy() {
        bluetoothController.close()
        super.onDestroy()
    }
}
