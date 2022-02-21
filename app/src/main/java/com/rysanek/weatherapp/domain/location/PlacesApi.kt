package com.rysanek.weatherapp.domain.location

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.ApiException
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.flow.flow

/**
 * This class checks for location permissions and handles when
 * the user denies any permissions by explaining the rationale
 * if needed and launching an [AlertDialog] letting the user
 * know when these permissions are needed.
 *
 * Once permission are checked an api request is made to
 * retrieve the [latLong] values or the current
 * geographic position of the device.
 *
 * The geographic position is then use to make another
 * call to the api to retrieve weather information
 * from the closest weather station.
 * **/
class PlacesApi(context: Application, apiKey: String) {

    private val TAG = "PlacesApi"
    private var _placeClient: PlacesClient? = null
    private val placesClient get()= _placeClient!!
    val latLong = MutableLiveData<LocationState>()

    init {
        Places.initialize(context, apiKey)
        _placeClient = Places.createClient(context)
    }

    @SuppressLint("MissingPermission")
    fun requestCurrentLatLong() {

        latLong.value = LocationState.Requesting

        // Use fields to define the data types to return.
        val placeFields: List<Place.Field> = listOf(Place.Field.LAT_LNG)

        // Use the builder to create a FindCurrentPlaceRequest.
        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

        val placeResponse = placesClient.findCurrentPlace(request)

        placeResponse.addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val response = task.result

                response.placeLikelihoods[0].place.latLng?.let { latLng ->

                    latLong.value = LocationState.Success(latLng)

                    Log.d(TAG, "right here lat: ${latLng.latitude}, long: ${latLng.longitude} ")
                }
            } else {
                val exception = task.exception
                if (exception is ApiException) {
                    Log.e(TAG, "Place not found: ${exception.statusCode}")
                }

                latLong.value = LocationState.Error(exception?.message ?: "An Error Occurred")
            }
        }
    }
    /**
     * Destroys the [PlacesClient]
     * **/
    fun onDestroyPlacesClient() {
        _placeClient = null
    }

}