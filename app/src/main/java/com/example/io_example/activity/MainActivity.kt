package com.example.io_example.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily.Companion.Monospace
import androidx.compose.ui.text.font.FontFamily.Companion.SansSerif
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.getString
import com.example.io_example.service.RestartService
import com.example.io_example.R
import com.example.io_example.ui.theme.Io_exampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Io_exampleTheme {
                MainScreen(
                    appNameText = getString(R.string.app_name),
                    gpioText = getString(R.string.gpio),
                    i2cText = getString(R.string.i2c),
                    serialText = getString(R.string.serial),
                    bluetoothText = getString(R.string.bluetooth),
                    onGpioClick = { startActivity(Intent(this, GpioActivity::class.java)) },
                    onI2cClick = { startActivity(Intent(this, I2cActivity::class.java)) },
                    onSerialClick = { startActivity(Intent(this, SerialActivity::class.java)) },
                    onBluetoothClick = { startActivity(Intent(this, BluetoothActivity::class.java)) }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        val serviceIntent = Intent(this, RestartService::class.java)
        stopService(serviceIntent)
        startService(serviceIntent)
    }
}

@Composable
fun MainScreen(
    appNameText: String,
    gpioText: String,
    i2cText: String,
    serialText: String,
    bluetoothText: String,
    onGpioClick: () -> Unit,
    onI2cClick: () -> Unit,
    onSerialClick: () -> Unit,
    onBluetoothClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.01f))

        Text(
            text = appNameText,
            fontSize = 30.sp,
            fontFamily = Monospace,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = onGpioClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB5FBA7)),
            modifier = Modifier
                .width(220.dp)
                .height(70.dp)
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = gpioText,
                fontSize = 28.sp,
                fontFamily = SansSerif,
                color = Color.Black
            )
        }

        Button(
            onClick = onI2cClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB5FBA7)),
            modifier = Modifier
                .width(220.dp)
                .height(70.dp)
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = i2cText,
                fontSize = 28.sp,
                fontFamily = SansSerif,
                color = Color.Black
            )
        }

        Button(
            onClick = onSerialClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB5FBA7)),
            modifier = Modifier
                .width(220.dp)
                .height(70.dp)
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = serialText,
                fontSize = 28.sp,
                fontFamily = SansSerif,
                color = Color.Black
            )
        }

        Button(
            onClick = onBluetoothClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB5FBA7)),
            modifier = Modifier
                .width(220.dp)
                .height(70.dp)
                .padding(vertical = 4.dp)
        ) {
            Text(
                text = bluetoothText,
                fontSize = 28.sp,
                fontFamily = SansSerif,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.weight(0.01f))
    }
}

@Preview(
    backgroundColor = 0xFFFFFFFF,
    showBackground = true
)
@Composable
fun MainScreenPreview() {
    Io_exampleTheme {
        MainScreen(
            appNameText = "Io_example",
            gpioText = "GPIO",
            i2cText = "I2C",
            serialText = "SERIAL",
            bluetoothText = "BLUETOOTH",
            onGpioClick = {},
            onI2cClick = {},
            onSerialClick = {},
            onBluetoothClick = {}
        )
    }
}
