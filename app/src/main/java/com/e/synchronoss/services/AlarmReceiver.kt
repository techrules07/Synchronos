package com.e.synchronoss.services

import android.app.ActivityManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.content.ContextCompat


class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (!isServiceRunning(SchedulerService::class.java, context!!)) {
            try {
                context.startService(Intent(context, SchedulerService::class.java))
            } catch (e: Exception) {
                e.printStackTrace()
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    val serviceIntent = Intent(context, SchedulerService::class.java)
                    ContextCompat.startForegroundService(context, serviceIntent)
                }
            }
        } else {

        }

    }

    fun isServiceRunning(
        serviceClass: Class<*>,
        context: Context
    ): Boolean {
        val manager =
            context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }

}