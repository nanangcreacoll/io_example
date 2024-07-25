package com.example.io_example.controller

import android.util.Log

class I2c (bus: Int, address: Byte): Command() {
    private val i2cBus: Int = bus
    private val i2cAddress: Byte = address
    private val writeCommand = "i2cset -y ${this.i2cBus} 0x${this.i2cAddress.toHexString()}"
    private val readCommand: String = "i2cget -y ${this.i2cBus} 0x${this.i2cAddress.toHexString()}"
    private val initCommand = "i2cdetect -y ${this.i2cBus}"

    init {
        Log.d(TAG, readRootCommand(this.initCommand))
        Log.d(TAG, "I2c init")
    }

    companion object {
        private const val TAG = "I2c"
    }

    fun write(data: Byte) {
        while (!executeRootCommand("${this.writeCommand} 0x${data.toHexString()}")) {
            Thread.sleep(10)
        }
    }

    fun read(): String {
        var result: String = readRootCommand(this.readCommand)
        while (result == "") {
            Thread.sleep(10)
            result = readRootCommand(this.readCommand)
        }
        return result
    }

    private fun Byte.toHexString(): String {
        return String.format("%02X", this)
    }
}