package com.e.synchronoss.services

import com.e.synchronoss.model.WeatherModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface BaseService {

    @GET("weather?")
    fun searchMovies(
        @Query("id") type: String?,
        @Query("appid") key: String?
    ): Call<WeatherModel?>?

}