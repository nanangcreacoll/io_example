package com.example.io_example.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily.Companion.SansSerif
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.io_example.controller.I2c
import com.example.io_example.R
import com.example.io_example.ui.theme.Io_exampleTheme

class I2cActivity : ComponentActivity() {
    private val i2cBus: Int = 4
    private val i2cAddress: Byte = 0x20
    private var value: Byte = 0x00

    private val i2c = I2c(i2cBus, i2cAddress)

    @OptIn(ExperimentalStdlibApi::class)
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Io_exampleTheme {
                I2cScreen(
                    i2cText = getString(R.string.i2c),
                    i2cWriteText = getString(R.string.i2c_write_button),
                    i2cReadText = getString(R.string.i2c_read_button),
                    initialI2cValueText = getString(R.string.i2cValue),
                    onWriteClick = {
                        writeI2c()
                    },
                    onReadClick = {
                        readI2c()
                    }
                )
            }
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    private fun writeI2c(): String {
        val displayText: String
        if (value < 0xff) {
            i2c.write(value)
            displayText = "Written: 0x${value.toHexString()}"
            value++
        } else {
            value = 0x00
            displayText = "Written: 0x${value.toHexString()}"
        }
        return displayText
    }

    private fun readI2c(): String {
        return i2c.read()
    }
}

@Composable
fun I2cScreen(
    i2cText: String,
    i2cWriteText: String,
    i2cReadText: String,
    initialI2cValueText: String,
    onWriteClick: () -> String,
    onReadClick: () -> String
) {
    var i2cValue by remember { mutableStateOf(initialI2cValueText) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.01f))

        Text(
            text = i2cText,
            fontSize = 30.sp,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = { i2cValue = onWriteClick() },
            modifier = Modifier
                .width(220.dp)
                .height(70.dp)
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB5FBA7))
        ) {
            Text(
                text = i2cWriteText,
                fontSize = 28.sp,
                fontFamily = SansSerif,
                color = Color.Black
            )
        }

        Button(
            onClick = { i2cValue = onReadClick() },
            modifier = Modifier
                .width(220.dp)
                .height(70.dp)
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB5FBA7))
        ) {
            Text(
                text = i2cReadText,
                fontSize = 28.sp,
                fontFamily = SansSerif,
                color = Color.Black
            )
        }

        Text(
            text = i2cValue,
            fontSize = 45.sp,
            fontFamily = SansSerif,
            color = Color.Black,
            modifier = Modifier
                .padding(vertical = 16.dp)
        )

        Spacer(modifier = Modifier.weight(0.01f))
    }
}

@Preview(
    backgroundColor = 0xFFFFFFFF,
    showBackground = true
)
@Composable
fun I2cScreenPreview() {
    Io_exampleTheme {
        I2cScreen(
            i2cText = "I2C",
            i2cWriteText = "I2C Write",
            i2cReadText = "I2C Read",
            initialI2cValueText = "0x00",
            onWriteClick = { "0x00" },
            onReadClick = { "0x00" }
        )
    }
}
