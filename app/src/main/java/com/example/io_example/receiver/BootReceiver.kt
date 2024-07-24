package com.example.io_example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.io_example.service.RestartService

class BootReceiver: BroadcastReceiver() {
    companion object {
        private const val TAG = "BootReceiver"
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.d(TAG, "Received boot completed broadcast")
            val serviceIntent = Intent(context, RestartService::class.java)
            context?.startService(serviceIntent)
        }
    }
}