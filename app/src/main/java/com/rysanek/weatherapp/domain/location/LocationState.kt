package com.rysanek.weatherapp.domain.location

import com.google.android.gms.maps.model.LatLng

sealed class LocationState{
    object Requesting: LocationState()
    data class Success(val data: LatLng): LocationState()
    data class Error(val message: String): LocationState()
}