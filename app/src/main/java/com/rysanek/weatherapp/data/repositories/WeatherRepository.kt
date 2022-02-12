package com.rysanek.weatherapp.data.repositories

import com.rysanek.weatherapp.data.remote.dtos.WeatherDto
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface WeatherRepository {

    suspend fun getCurrentLocalWeather(lat: Float, long: Float): Flow<Response<WeatherDto>>
}