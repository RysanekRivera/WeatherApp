package com.rysanek.weatherapp.presentation.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rysanek.weatherapp.data.remote.dtos.WeatherDto
import com.rysanek.weatherapp.domain.location.LocationState
import com.rysanek.weatherapp.domain.usecases.FetchCurrentWeatherData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val fetchCurrentWeatherData: FetchCurrentWeatherData
) : ViewModel() {
    private val TAG = "WeatherViewModel"

    private var _weatherData = MutableLiveData<WeatherDto>()
    val weatherData get() = _weatherData

    private var _isLocationPermissionGranted = MutableLiveData<Boolean>()
    val isLocationPermissionGranted get() = _isLocationPermissionGranted

    val latLong = fetchCurrentWeatherData.latLong

    fun requestCurrentLocationLatLong() = fetchCurrentWeatherData.requestCurrentLocationLatLong()

    fun handleLocationState(state: LocationState) {
        when (state) {
            is LocationState.Success -> {

                viewModelScope.launch {
                    val latitude = state.data.latitude.toFloat()
                    val longitude = state.data.longitude.toFloat()
                    Log.d("WeatherViewModel", "Success: lat: $latitude, long: $longitude")

                    fetchCurrentWeatherData.fetchCurrentWeatherData(latitude, longitude)
                        .catch { e -> Log.e(TAG, e.message ?: "An Error Occurred") }
                        .filter { response -> response.isSuccessful && response.body() != null }
                        .collect { response ->
                            Log.d(TAG, "response: $response")
                            _weatherData.postValue(response.body())
                        }
                }
            }

            is LocationState.Error -> {
                Log.d("WeatherViewModel", "Error: ${state.message}")
            }

            is LocationState.Requesting -> {
                Log.d("WeatherViewModel", "Requesting Location")
            }
        }
    }

    fun checkLocationPermission(isGranted: Boolean) {
        _isLocationPermissionGranted.postValue(isGranted)
    }

    override fun onCleared() {
        super.onCleared()
        fetchCurrentWeatherData.destroyPlacesClient()
    }

}