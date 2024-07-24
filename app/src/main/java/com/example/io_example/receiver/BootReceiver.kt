package com.example.io_example.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.io_example.service.RestartService

class BootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            val serviceIntent = Intent(context, RestartService::class.java)
            context?.startService(serviceIntent)
        }
    }
}