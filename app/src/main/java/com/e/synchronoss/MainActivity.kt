package com.e.synchronoss

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.e.synchronoss.model.WeatherModel
import com.e.synchronoss.services.AlarmReceiver
import com.e.synchronoss.services.SchedulerService
import com.e.synchronoss.utils.ManageSharedPreference
import com.e.synchronoss.viewmodels.MainViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*


class MainActivity : AppCompatActivity() {

    private var viewModel: MainViewModel? = null
    var weather: WeatherModel? = null

    private var manageSharedPreferences: ManageSharedPreference? = null
    var WEATHER_DATA = "weather_data"
    var myService: SchedulerService? = null
    var RELOAD = "reload_data"

    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            val binder: SchedulerService.LocalBinder = service as SchedulerService.LocalBinder
            myService = binder.getService()
        }

        override fun onServiceDisconnected(name: ComponentName) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel!!.init()
        manageSharedPreferences = ManageSharedPreference().newInstance()

        if(manageSharedPreferences!!.getString(applicationContext,WEATHER_DATA)!=null){
            weather = convertJsonToDataObject(manageSharedPreferences!!.getString(applicationContext,WEATHER_DATA))

            if(manageSharedPreferences!!.getString(applicationContext,RELOAD).equals("true")){
                viewModel!!.getWeather("7778677","5ad7218f2e11df834b0eaf3a33a39d2a")
            }
            else{
                setWeatherData(weather)
            }

        }
        else{
                viewModel!!.getWeather("7778677","5ad7218f2e11df834b0eaf3a33a39d2a")

        }


        viewModel!!?.getWeatherData()
            ?.observe(this, Observer<Any?> { response ->
                if (response != null) {
                    if(response is WeatherModel){

                        manageSharedPreferences!!.saveString(applicationContext,convertObjectToJson(response), WEATHER_DATA)
                        manageSharedPreferences!!.saveString(applicationContext,"false",RELOAD)

                        setWeatherData(response)

                    }

                }
            })



        startBroadcastReceiver()

    }

    private fun setWeatherData(weather: WeatherModel?) {
        txt_city.text = weather!!.name.toString()
        txt_temperature.text = (Math.round(weather!!.main!!.temp!!)?.minus(273.0)).toInt().toString()+ " \u2103"
        txt_max_temperature.text = (Math.round(weather!!.main!!.temp_max!!)?.minus(273.0)).toInt().toString()+ "\u2103"
        txt_min_temperature.text = (Math.round(weather!!.main!!.temp_min!!)?.minus(273.0)).toInt().toString()+ "\u2103"
        if(weather!!.weather!=null && weather!!.weather!!.size>0){
            txt_weather.text = weather!!.weather!![0].main+"-"+weather!!.weather!![0].description
        }
        txt_humidity.text = "Humidity "+weather!!.main!!.humidity+"%"
        txt_wind.text = "Wind Speed "+weather!!.wind!!.speed+" km/hr"
    }


    private fun startBroadcastReceiver() {
        val alarmMgr =
            getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, AlarmReceiver::class.java)
        val alarmIntent =
            PendingIntent.getBroadcast(this, 1545, intent, 0)
        alarmMgr[AlarmManager.RTC, System.currentTimeMillis()] = alarmIntent
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun convertObjectToJson(`object`: Any?): String? {
        val gson = Gson()
        return gson.toJson(`object`)
    }


    fun convertJsonToDataObject(json: String?): WeatherModel? {
        if (json == null) {
            return null
        }
        val gson = Gson()
        return gson.fromJson(json, WeatherModel::class.java)
    }

}
