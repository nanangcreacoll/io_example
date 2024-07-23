package com.example.io_example.controller

import android.util.Log
import tp.xmaihh.serialport.SerialHelper
import tp.xmaihh.serialport.bean.ComBean

class Serial (port: String, baudRate: Int): SerialHelper(port, baudRate){
    companion object {
        private const val TAG = "Serial"
    }

    var data: String = ""

    public override fun onDataReceived(comBean: ComBean) {
        val receivedData = String(comBean.bRec)
        Log.d(TAG, "Received data: $receivedData")
        data = receivedData
    }
}