package com.e.synchronoss.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.e.synchronoss.utils.ManageSharedPreference
import com.e.synchronoss.viewmodels.MainViewModel
import java.util.*


class SchedulerService : Service() {

    private val mBinder: IBinder =LocalBinder()
    var timer: Timer? = null
    private var viewModel: MainViewModel? = null
    private var manageSharedPreferences: ManageSharedPreference? = null
    var RELOAD = "reload_data"

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        return START_STICKY;
    }

    override fun onBind(intent: Intent?): IBinder? {
        return mBinder
    }

    override fun onCreate() {
        super.onCreate()

        manageSharedPreferences = ManageSharedPreference().newInstance()

        timer = Timer()
        timer!!.scheduleAtFixedRate(
            object : TimerTask() {

                override fun run() {
                    manageSharedPreferences!!.saveString(applicationContext,"true",RELOAD)
                }

            },0, 60 * 60 * 1000 * 2
        )

    }

    class LocalBinder : Binder() {
        fun getService(): SchedulerService? {
            return SchedulerService()
        }
    }

}