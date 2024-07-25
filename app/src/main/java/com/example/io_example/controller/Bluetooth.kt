package com.example.io_example.controller

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.*
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.util.*

@SuppressLint("MissingPermission")
class Bluetooth(
    private val activity: Activity,
    private val bleList: List<List<String>>,
    private val bleCharacteristicValue: (String, String) -> Unit
) {

    private var manager: BluetoothManager = activity.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    private var gatt: BluetoothGatt? = null

    init {
        if (!manager.adapter.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivity(enableBtIntent)
        }

        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), PERMISSION_REQUEST_CODE
            )
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    fun startScanning() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            manager.adapter.bluetoothLeScanner.startScan(BleScanCallback())
        } else {
            Log.d(TAG, "Permissions not granted")
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ), PERMISSION_REQUEST_CODE
            )
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    private inner class BleScanCallback : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            for (device in bleList) {
                if (result?.device?.address == device[0]) {
                    gatt = result.device.connectGatt(activity, false, BleGattCallback())
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
            for (device in bleList) {
                if (gatt?.device?.address == device[0]) {
                    bleCharacteristic = this@Bluetooth.gatt
                        ?.getService(UUID.fromString(device[1]))
                        ?.getCharacteristic(UUID.fromString(device[2]))
                        ?: continue
                    Log.d(TAG, "Service discovered for device: ${gatt.device?.address}")

                    gatt.setCharacteristicNotification(bleCharacteristic, true)
                    bleDescriptor = bleCharacteristic.getDescriptor(UUID.fromString(device[3]))
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                        if (bleCharacteristic.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0) {
                            Log.d(TAG, "Property indicate")
                            bleDescriptor.value = BluetoothGattDescriptor.ENABLE_INDICATION_VALUE
                            gatt.writeDescriptor(bleDescriptor)
                        } else if (bleCharacteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
                            Log.d(TAG, "Property notify")
                            bleDescriptor.value = BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE
                            gatt.writeDescriptor(bleDescriptor)
                        }
                    } else {
                        if (bleCharacteristic.properties and BluetoothGattCharacteristic.PROPERTY_INDICATE != 0) {
                            Log.d(TAG, "Property indicate")
                            gatt.writeDescriptor(bleDescriptor, BluetoothGattDescriptor.ENABLE_INDICATION_VALUE)
                        } else if (bleCharacteristic.properties and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
                            Log.d(TAG, "Property notify")
                            gatt.writeDescriptor(bleDescriptor, BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE)
                        }
                    }
                    break
                }
            }

            super.onServicesDiscovered(gatt, status)
        }

        override fun onDescriptorWrite(gatt: BluetoothGatt?, descriptor: BluetoothGattDescriptor?, status: Int) {
            Log.d(TAG, if (status == BluetoothGatt.GATT_SUCCESS) "Descriptor written successfully" else "Failed to write descriptor")
            super.onDescriptorWrite(gatt, descriptor, status)
        }

        @Deprecated("Deprecated in Java")
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
        ) {
            val deviceAddress = gatt.device?.address
            handleCharacteristicChanged(deviceAddress, characteristic.value)
            super.onCharacteristicChanged(gatt, characteristic)
        }

        @RequiresApi(Build.VERSION_CODES.TIRAMISU)
        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            val deviceAddress = gatt.device?.address
            handleCharacteristicChanged(deviceAddress, value)
            super.onCharacteristicChanged(gatt, characteristic, value)
        }

        private fun handleCharacteristicChanged(deviceAddress: String?, value: ByteArray) {
            val characteristicValue = value.toList().map { it.toUByte() }.toString()
            Log.d(TAG, "Characteristic changed for device: $deviceAddress with value: $characteristicValue")
            if (deviceAddress != null) {
                bleCharacteristicValue(deviceAddress, characteristicValue)
            }
            bleCharacteristicValue(deviceAddress ?: "", characteristicValue)
        }
    }

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    fun close() {
        gatt?.let {
            it.disconnect()
            it.close()
        }
    }

    companion object {
        private const val TAG = "Bluetooth"
        private const val PERMISSION_REQUEST_CODE = 1
    }
}
