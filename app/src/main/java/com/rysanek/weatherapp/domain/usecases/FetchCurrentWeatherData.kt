package com.rysanek.weatherapp.domain.usecases

import android.util.Log
import com.rysanek.weatherapp.data.repositories.WeatherRepositoryImpl
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import javax.inject.Inject

class FetchCurrentWeatherData @Inject constructor(
    private val repository: WeatherRepositoryImpl
) {
    private val TAG = "FetchCurrentWeatherData"

    suspend fun fetchCurrentWeatherData(lat: Float, long: Float) = repository.getCurrentLocalWeather(lat, long)
        .catch { e -> Log.e(TAG, e.message ?: "An Error Occurred") }
        .filter { response -> response.isSuccessful && response.body() != null }
}