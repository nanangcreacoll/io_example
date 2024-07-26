package com.example.io_example.activity

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
import androidx.compose.ui.text.font.FontFamily.Companion.SansSerif
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.io_example.controller.Gpio
import com.example.io_example.R
import com.example.io_example.ui.theme.Io_exampleTheme

class GpioActivity : ComponentActivity() {
    private val whiteLedPin = 11 // GPIO0_B3
    private val greenLedPin = 1 // GPIO0_A1
    private val redLedPin = 13 // GPIO0_B5

    private val whiteLed: Gpio = Gpio(whiteLedPin)
    private val greenLed: Gpio = Gpio(greenLedPin)
    private val redLed: Gpio = Gpio(redLedPin)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Io_exampleTheme {
                GpioScreen(
                    gpioText = getString(R.string.gpio),
                    whiteLedText = getString(R.string.white_led_button),
                    greenLedText = getString(R.string.green_led_button),
                    redLedText = getString(R.string.red_led_button),
                    onWhiteLedClick = { toggleLed(whiteLed) },
                    onGreenLedClick = { toggleLed(greenLed) },
                    onRedLedClick = { toggleLed(redLed) }
                )
            }
        }
    }

    private fun toggleLed(led: Gpio) {
        if (led.isOn) {
            led.off()
        } else {
            led.on()
        }
    }
}

@Composable
fun GpioScreen(
    gpioText: String,
    whiteLedText: String,
    greenLedText: String,
    redLedText: String,
    onWhiteLedClick: () -> Unit,
    onGreenLedClick: () -> Unit,
    onRedLedClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.01f))

        Text(
            text = gpioText,
            fontSize = 30.sp,
            modifier = Modifier
                .padding(vertical = 8.dp)
        )

        Button(
            onClick = onWhiteLedClick,
            modifier = Modifier
                .width(220.dp)
                .height(70.dp)
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF787878))
        ) {
            Text(
                text = whiteLedText,
                fontSize = 28.sp,
                fontFamily = SansSerif,
                color = Color.White
            )
        }

        Button(
            onClick = onGreenLedClick,
            modifier = Modifier
                .width(220.dp)
                .height(70.dp)
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6FFF52))
        ) {
            Text(
                text = greenLedText,
                fontSize = 28.sp,
                fontFamily = SansSerif,
                color = Color.Black
            )
        }

        Button(
            onClick = onRedLedClick,
            modifier = Modifier
                .width(220.dp)
                .height(70.dp)
                .padding(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFD6666))
        ) {
            Text(
                text = redLedText,
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
fun GpioScreenPreview() {
    Io_exampleTheme {
        GpioScreen(
            gpioText = "GPIO",
            whiteLedText = "WHITE LED",
            greenLedText = "GREEN LED",
            redLedText = "RED LED",
            onWhiteLedClick = {},
            onGreenLedClick = {},
            onRedLedClick = {}
        )
    }
}
