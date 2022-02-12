package com.rysanek.weatherapp.presentation.activities

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.libraries.places.api.net.PlacesClient
import com.rysanek.weatherapp.BuildConfig.PLACES_API_KEY
import com.rysanek.weatherapp.R
import com.rysanek.weatherapp.data.repositories.WeatherRepositoryImpl
import com.rysanek.weatherapp.domain.location.PlacesApi
import com.rysanek.weatherapp.presentation.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val placesApi = PlacesApi()
    private lateinit var placesClient: PlacesClient
    @Inject lateinit var repository: WeatherRepositoryImpl
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        placesClient = placesApi(application, PLACES_API_KEY)

        placesApi.checkLocationPermissions(this, placesClient)

        viewModel.fetchCurrentWeatherData(placesApi.lat ?: 40.76347252582717.toFloat(), placesApi.long ?: (-73.98628004944972).toFloat())

        viewModel.weatherData.observe(this) {
            Log.d("MainActivity", "data: $it")
        }

        setContentView(R.layout.activity_main)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        handlePermissionDenials(requestCode, permissions, grantResults)
    }

    private fun handlePermissionDenials(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 100 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //NO-OP
        } else {
            if (permissions.isNotEmpty())
                permissions.forEach { permission ->
                    showRationaleIfNeeded(permission, requestCode)
                }
        }
    }

    private fun showRationaleIfNeeded(permission: String, requestCode: Int) {
        if (shouldShowRequestPermissionRationale(permission)) {
            AlertDialog.Builder(this)
                .setTitle("$permission Needed")
                .setMessage("Accept $permission Permission")
                .setPositiveButton("Accept") { _, _ ->
                    requestPermissions(arrayOf(permission), requestCode)
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .create()
                .show()
        } else {
            AlertDialog.Builder(this)
                .setTitle("Permission Needed")
                .setMessage("Permissions are needed for proper functionality of the app.")
                .setPositiveButton("Settings") { _, _ ->
                    Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                        addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        data = Uri.fromParts("package", packageName, null)
                        startActivity(this)
                    }
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .create()
                .show()
        }
    }
}



