package com.rysanek.weatherapp.data.remote.apis

import com.rysanek.weatherapp.BuildConfig
import com.rysanek.weatherapp.data.remote.dtos.WeatherDto
import com.rysanek.weatherapp.domain.utils.Constants.IMPERIAL_UNITS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("data/2.5/weather")
    suspend fun fetchCurrentWeather(
        @Query("appid") apiKey: String = BuildConfig.WEATHER_API_KEY,
        @Query("units") units: String = IMPERIAL_UNITS,
        @Query("lat") lat: Float,
        @Query("lon") long: Float,
    ): Response<WeatherDto>
}