package com.example.io_example.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily.Companion.SansSerif
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.io_example.controller.Serial
import com.example.io_example.ui.theme.Io_exampleTheme
import com.example.io_example.R
import com.example.io_example.ui.theme.LightGreen
import java.io.IOException
import java.security.InvalidParameterException

class SerialActivity : ComponentActivity() {
    companion object {
        private const val TAG = "SerialActivity"
    }

    private val serialPortPath = "/dev/ttyS3" // rockchip serial UART ttyS3
    private val baudRate = 115200
    private lateinit var serialHelper: Serial

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        serialHelper = Serial(serialPortPath, baudRate)

        try {
            serialHelper.open()
        } catch (e: SecurityException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: InvalidParameterException) {
            e.printStackTrace()
        }

        setContent {
            Io_exampleTheme {
                SerialScreen(
                    serialText = getString(R.string.serial),
                    serialSendText = getString(R.string.serial_send),
                    serialValueText = getString(R.string.serialValue),
                    onSendClick = { input1, input2 ->
                        sendData(input1, input2, serialHelper)
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serialHelper.close()
    }

    private fun sendData(input1: String, input2: String, serial: Serial): String {
        if (serial.isOpen) {
            val sendData = "$input1 $input2"
            serial.sendTxt("$sendData\r\n")
            Log.d(TAG, "Sent data: $sendData, with CRLF")
            while (serial.data.isEmpty()) {
                Thread.sleep(10)
            }
            val result = serial.data
            serial.data = ""
            return result
        } else {
            Log.e(TAG, "Serial port is not open")
            return "Error: Serial port is not open"
        }
    }
}

@Composable
fun SerialScreen(
    serialText: String,
    serialSendText: String,
    serialValueText: String,
    onSendClick: (String, String) -> String
) {
    var input1 by remember { mutableStateOf("") }
    var input2 by remember { mutableStateOf("") }
    var result by remember { mutableStateOf(serialValueText) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.01f))

        Text(
            text = serialText,
            fontSize = 30.sp,
            modifier = Modifier.padding(vertical = 8.dp)
        )

        TextField(
            value = input1,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { input1 = it },
            label = { Text(text = "Input 1") },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .width(220.dp)
        )

        TextField(
            value = input2,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            onValueChange = { input2 = it },
            label = { Text(text = "Input 2") },
            modifier = Modifier
                .padding(vertical = 4.dp)
                .width(220.dp)
        )

        Button(
            onClick = { result = onSendClick(input1, input2) },
            modifier = Modifier
                .width(220.dp)
                .height(70.dp)
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = LightGreen)
        ) {
            Text(
                text = serialSendText,
                fontSize = 28.sp,
                fontFamily = SansSerif,
                color = Color.Black
            )
        }

        Text(
            text = result,
            fontSize = 45.sp,
            fontFamily = SansSerif,
            color = Color.Black,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Spacer(modifier = Modifier.weight(0.01f))
    }
}

@Preview(showBackground = true)
@Composable
fun SerialScreenPreview() {
    Io_exampleTheme {
        SerialScreen(
            serialText = "Serial",
            serialSendText = "Send",
            serialValueText = "Value",
            onSendClick = { _, _ -> "Result" }
        )
    }
}
