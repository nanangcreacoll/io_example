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
    private lateinit var bluetoothValueView: TextView

    // MAC address, service UUID, characteristic UUID, notification descriptor UUID
    private val bleList: List<List<String>> = listOf(
        listOf("17:71:12:4E:C6:95", "0000ffe0-0000-1000-8000-00805f9b34fb", "0000ffe4-0000-1000-8000-00805f9b34fb", "00002902-0000-1000-8000-00805f9b34fb")
    )

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_bluetooth)

        bluetoothStartButton = findViewById(R.id.bluetoothStartButton)
        bluetoothValueView = findViewById(R.id.bluetoothValueView)

        bluetoothController = Bluetooth(this, bleList) { value ->
            runOnUiThread { bluetoothValueView.text = value }
        }

        bluetoothStartButton.setOnClickListener {
            bluetoothController.startScanning()
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onDestroy() {
        bluetoothController.close()
        super.onDestroy()
    }
}
