package com.rysanek.weatherapp.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rysanek.weatherapp.data.remote.dtos.WeatherDto
import com.rysanek.weatherapp.domain.usecases.FetchCurrentWeatherData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val fetchCurrentWeatherData: FetchCurrentWeatherData
): ViewModel() {

    private var _weatherData = MutableLiveData<WeatherDto>()
    val weatherData get() = _weatherData

    fun fetchCurrentWeatherData(lat: Float, long: Float) = viewModelScope.launch(Dispatchers.IO) {
        fetchCurrentWeatherData.fetchCurrentWeatherData(lat, long).collect { response ->
            _weatherData.postValue(response.body())
        }
    }

}