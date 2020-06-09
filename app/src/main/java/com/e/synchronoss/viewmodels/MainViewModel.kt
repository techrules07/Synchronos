package com.e.synchronoss.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.e.synchronoss.model.WeatherModel
import com.e.synchronoss.repositories.Repositories


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var repositories: Repositories? = null
    private var responseLiveData: LiveData<WeatherModel?>? = null
//    private var responseLiveDataDetails: LiveData<MovieDetailsResponse?>? = null

    fun SearchViewModel(application: Application) {
//        super(application)
    }

    fun init() {
        repositories = Repositories()
        responseLiveData = repositories!!.getWeatherResponseLiveData()
//        responseLiveDataDetails = repositories!!.getMovieDetailsResponseLiveData()
    }

    fun getWeather(id: String?, apiKey: String?) {
        repositories!!.getWeather(id, apiKey)
    }

    fun getWeatherData(): LiveData<WeatherModel?>? {
        return responseLiveData
    }

//    fun movieDetails(plot: String?, apiKey: String?, t: String?) {
//        repositories!!.movieDetails(plot, apiKey, t)
//    }
//
//    fun getMovieDetails(): LiveData<MovieDetailsResponse?>? {
//        return responseLiveDataDetails
//    }
}