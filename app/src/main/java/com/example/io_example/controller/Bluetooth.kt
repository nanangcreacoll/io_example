package com.example.io_example

import android.annotation.SuppressLint
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.util.*

@SuppressLint("MissingPermission")
class Bluetooth(
    private val context: Context,
    private val bleList: List<List<String>>,
    private val onUpdate: (String) -> Unit
) {

    private var manager: BluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private var gatt: BluetoothGatt? = null

    init {
        if (!manager.adapter.isEnabled) {
            manager.adapter.enable()
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    fun startScanning() {
        manager.adapter.bluetoothLeScanner.startScan(BleScanCallback())
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    private inner class BleScanCallback : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            for (dt in bleList) {
                if (result?.device?.address == dt[0]) {
                    gatt = result.device.connectGatt(context, false, BleGattCallback())
                    break
                }
            }
            super.onScanResult(callbackType, result)
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    private inner class BleGattCallback : BluetoothGattCallback() {
        private lateinit var bleCharacteristic: BluetoothGattCharacteristic
        private lateinit var bleDescriptor: BluetoothGattDescriptor

        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                gatt?.discoverServices()
                Log.d(TAG, "Connected to device: ${gatt?.device?.address}")
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Log.d(TAG, "Disconnected from device: ${gatt?.device?.address}")
            }
            super.onConnectionStateChange(gatt, status, newState)
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            for (dt in bleList) {
                if (gatt?.device?.address == dt[0]) {
                    bleCharacteristic = this@Bluetooth.gatt?.getService(UUID.fromString(dt[1]))?.getCharacteristic(UUID.fromString(dt[2]))
                        ?: break
                }
            }
            Log.d(TAG, "Service discovered")
            gatt!!.setCharacteristicNotification(bleCharacteristic, true)

            bleDescriptor = bleCharacteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"))
            bleDescriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE

            gatt.writeDescriptor(bleDescriptor)

            super.onServicesDiscovered(gatt, status)
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            Log.d(TAG, if (status == BluetoothGatt.GATT_SUCCESS) "Descriptor written successfully" else "Failed to write descriptor")
            super.onDescriptorWrite(gatt, descriptor, status)
        }

        override fun onCharacteristicChanged(gatt: BluetoothGatt, characteristic: BluetoothGattCharacteristic) {
            val value = characteristic.value.toList().map { it.toUByte() }.toString()
            Log.d(TAG, value)
            onUpdate(value)
            super.onCharacteristicChanged(gatt, characteristic)
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    fun close() {
        gatt?.disconnect()
        gatt?.close()
    }

    companion object {
        private const val TAG = "Bluetooth"
    }
}
