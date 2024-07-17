package com.example.io_example.io

import android.util.Log

class I2c (bus: Int, address: Byte): Command() {
    private var i2cData: Byte = 0
    private val i2cBus: Int = bus
    private val i2cAddress: Byte = address

    fun write(command: Byte) {
        executeRootCommand("i2cset","-y $i2cBus 0x${i2cAddress.toHexString()} 0x${command.toHexString()}")
    }

    fun read(): String {
        return readRootCommand("i2cget","-y $i2cBus 0x${i2cAddress.toHexString()}")
    }

    private fun Byte.toHexString(): String {
        return String.format("%02X", this)
    }

    private fun Int.toHexString(): String {
        return String.format("%02X", this)
    }
}