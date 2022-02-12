package com.rysanek.weatherapp.domain.location

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.rysanek.weatherapp.data.repositories.WeatherRepositoryImpl
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class PlacesApi {

    private val TAG = "PlacesApi"
    var lat: Float? = null
    var long: Float? = null

    operator fun invoke(context: Application, apiKey: String): PlacesClient{
        Places.initialize(context, apiKey)
        return Places.createClient(context)
    }

    fun checkLocationPermissions(activity: Activity, placesClient: PlacesClient){

        // Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {

            // Use fields to define the data types to return.
            val placeFields: List<Place.Field> = listOf(Place.Field.LAT_LNG)

            // Use the builder to create a FindCurrentPlaceRequest.
            val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

            val placeResponse = placesClient.findCurrentPlace(request)
            placeResponse.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val response = task.result
                    val latLng = response.placeLikelihoods[0].place.latLng
                    lat = latLng?.latitude?.toFloat()
                    long = latLng?.longitude?.toFloat()

                    Log.d(TAG, "lat: ${latLng?.latitude}, long: ${latLng?.longitude} ")
                } else {
                    val exception = task.exception
                    if (exception is ApiException) {
                        Log.e(TAG, "Place not found: ${exception.statusCode}")
                    }
                }
            }
        } else {
            // Request required permissions
            getLocationPermission(activity)
        }
    }

    private fun getLocationPermission(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            100
        )
    }

}