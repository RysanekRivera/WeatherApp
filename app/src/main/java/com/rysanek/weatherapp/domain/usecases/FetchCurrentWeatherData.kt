package com.rysanek.weatherapp.domain.usecases

import com.rysanek.weatherapp.data.repositories.WeatherRepositoryImpl
import com.rysanek.weatherapp.domain.location.PlacesApi
import javax.inject.Inject

class FetchCurrentWeatherData @Inject constructor(
    private val repository: WeatherRepositoryImpl,
    private val placesApi: PlacesApi
) {

    val latLong = placesApi.latLong

    fun requestCurrentLocationLatLong() = placesApi.requestCurrentLatLong()

    suspend fun fetchCurrentWeatherData(lat: Float, long: Float)  = repository.getCurrentLocalWeather(lat, long)

    fun destroyPlacesClient() = placesApi.onDestroyPlacesClient()
}