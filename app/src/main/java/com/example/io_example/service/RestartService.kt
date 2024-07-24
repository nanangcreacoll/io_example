package com.example.io_example.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.io_example.activity.MainActivity

class RestartService : Service() {
    companion object {
        private const val TAG = "RestartService"
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "RestartService started")

        if (!isActivityRunning(MainActivity::class.java)) {
            val activityIntent = Intent(this, MainActivity::class.java)
            activityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(activityIntent)
        }

        return START_STICKY
    }

    private fun isActivityRunning(activityClass: Class<*>): Boolean {
        val activityManager = getSystemService(ACTIVITY_SERVICE) as android.app.ActivityManager
        for (appTask in activityManager.appTasks) {
            if (appTask.taskInfo.topActivity?.className == activityClass.name) {
                return true
            }
        }
        return false
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}