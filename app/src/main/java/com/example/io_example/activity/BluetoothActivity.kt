package com.example.io_example.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.internal.enableLiveLiterals
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily.Companion.SansSerif
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.io_example.R
import com.example.io_example.controller.Bluetooth
import com.example.io_example.ui.theme.Io_exampleTheme

class BluetoothActivity : ComponentActivity() {
    private lateinit var bluetoothController: Bluetooth
    private val deviceValues = mutableStateMapOf<String, String>()

    private val bleList: List<List<String>> = listOf(
        listOf("17:71:12:4E:C6:95", "0000ffe0-0000-1000-8000-00805f9b34fb", "0000ffe4-0000-1000-8000-00805f9b34fb"), // Pulse Oximeter
        listOf("64:FB:01:16:98:AE", "0000fff0-0000-1000-8000-00805f9b34fb", "0000fff1-0000-1000-8000-00805f9b34fb"), // Weight Scale
        listOf("C0:30:00:31:D7:B8", "00001810-0000-1000-8000-00805f9b34fb", "00002a35-0000-1000-8000-00805f9b34fb") // Blood Pressure
    )

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        bluetoothController = Bluetooth(this, bleList) { address, value ->
            // Update the state on the main thread
            runOnUiThread {
                deviceValues[address] = value
            }
        }

        setContent {
            Io_exampleTheme {
                BluetoothScreen(
                    bluetoothText = getString(R.string.bluetooth),
                    bluetoothStartText = getString(R.string.bluetoothStartButton),
                    bluetoothDeviceText = getString(R.string.bluetoothDeviceValue),
                    deviceValues = deviceValues,
                    onStartClick = { bluetoothController.startScanning() }
                )
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onDestroy() {
        bluetoothController.close()
        super.onDestroy()
    }
}

@Composable
fun BluetoothScreen(
    bluetoothText: String,
    bluetoothStartText: String,
    bluetoothDeviceText: String,
    deviceValues: Map<String, String>,
    onStartClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.01f))

        Text(
            text = bluetoothText,
            fontSize = 30.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        Button(
            onClick = { onStartClick() },
            modifier = Modifier
                .width(200.dp)
                .height(70.dp)
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB5FBA7))
        ) {
            Text(
                text = bluetoothStartText,
                fontSize = 28.sp,
                fontFamily = SansSerif,
                color = Color.Black
            )
        }

        val displayText = if (deviceValues.isEmpty()) {
            bluetoothDeviceText
        } else {
            deviceValues.entries.joinToString("\n") { (address, value) -> "Device: $address\nValue: $value\n" }
        }

        Text(
            text = displayText,
            fontSize = 35.sp,
            fontFamily = SansSerif,
            color = Color.Black,
            lineHeight = 50.sp,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Spacer(modifier = Modifier.weight(0.01f))
    }
}

@Preview(showBackground = true)
@Composable
fun BluetoothScreenPreview() {
    Io_exampleTheme {
        BluetoothScreen(
            bluetoothText = "Bluetooth",
            bluetoothStartText = "Start",
            bluetoothDeviceText = "Device:00:00:00:00:00:00\nValue:[0]\n",
            deviceValues = mapOf(),
            onStartClick = {}
        )
    }
}
