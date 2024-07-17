package com.example.io_example.io

import android.util.Log
import tp.xmaihh.serialport.SerialHelper
import tp.xmaihh.serialport.bean.ComBean

class Serial (port: String, baudRate: Int): SerialHelper(port, baudRate){
    var data: String = ""

    public override fun onDataReceived(comBean: ComBean) {
        val receivedData = String(comBean.bRec)
        Log.d("SerialData", "Received data: $receivedData")
        data = receivedData
    }
}