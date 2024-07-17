package com.example.io_example

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import com.example.io_example.io.Serial
import java.io.IOException
import java.security.InvalidParameterException

class SerialActivity : ComponentActivity() {
    private val serialPortPath = "/dev/ttyACM0"
    private val baudRate = 115200
    private lateinit var serialHelper: Serial

    private lateinit var sendButton: Button
    private lateinit var input1: EditText
    private lateinit var input2: EditText
    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_serial)

        sendButton = findViewById(R.id.serialSendButton)
        input1 = findViewById(R.id.input1Number)
        input2 = findViewById(R.id.input2Number)
        result = findViewById(R.id.serialResultView)

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

        sendButton.setOnClickListener {
            sendData(input1.text.toString(), input2.text.toString(), serialHelper)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        serialHelper.close()
    }

    private fun sendData(input1: String, input2: String, serial: Serial) {
        if (serial.isOpen) {
            val sendData = "$input1 $input2"
            serial.sendTxt("$sendData\r\n")
            Log.d("SerialData", "Sent data: $sendData, with CRLF")
            while (serialHelper.data.isEmpty()) {}
            result.text = serialHelper.data
            serialHelper.data = ""
        } else {
            Log.e("SerialError", "Serial port is not open")
        }
    }
}