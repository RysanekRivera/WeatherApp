package com.rysanek.weatherapp.data.repositories

import com.rysanek.weatherapp.data.remote.apis.WeatherApi
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
): WeatherRepository {

    override suspend fun getCurrentLocalWeather(lat: Float, long: Float) = flow {  emit(api.fetchCurrentWeather(lat = lat, long = long)) }
}