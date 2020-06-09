package com.e.synchronoss.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.e.synchronoss.model.WeatherModel
import com.e.synchronoss.services.BaseService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Repositories {
    private val SERVICE_BASE_URL = "http://api.openweathermap.org/data/2.5/"

    private var baseService: BaseService? = null
    private var responseLiveData: MutableLiveData<WeatherModel?>? = null
//    private var responseLiveDataDetails: MutableLiveData<MovieDetailsResponse?>? = null

//    constructor(responseLiveData: MutableLiveData<SearchResponse?>?) {
//        this.responseLiveData = responseLiveData
//    }


    constructor() {
        responseLiveData = MutableLiveData<WeatherModel?>()
//        responseLiveDataDetails = MutableLiveData<MovieDetailsResponse?>()

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client =
            OkHttpClient.Builder().addInterceptor(interceptor).build()
        baseService = Retrofit.Builder()
            .baseUrl(SERVICE_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create<BaseService>(BaseService::class.java)
    }

    fun getWeather(id: String?, appid: String?) {

        baseService!!.searchMovies(id, appid)
            ?.enqueue(object : Callback<WeatherModel?> {
                override fun onResponse(
                    call: Call<WeatherModel?>,
                    response: Response<WeatherModel?>
                ) {
                    if (response.body() != null) {
                        responseLiveData!!.postValue(response.body())
                    }
                }

                override fun onFailure(
                    call: Call<WeatherModel?>,
                    t: Throwable
                ) {
                    responseLiveData!!.postValue(null)
                }
            })
    }

    fun getWeatherResponseLiveData(): LiveData<WeatherModel?>? {
        return responseLiveData
    }

}