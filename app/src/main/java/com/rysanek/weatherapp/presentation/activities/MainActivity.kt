package com.rysanek.weatherapp.presentation.activities

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.rysanek.weatherapp.data.repositories.WeatherRepositoryImpl
import com.rysanek.weatherapp.databinding.ActivityMainBinding
import com.rysanek.weatherapp.domain.permissions.handlePermissionDenials
import com.rysanek.weatherapp.domain.permissions.isLocationPermissionGranted
import com.rysanek.weatherapp.presentation.viewmodels.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var repository: WeatherRepositoryImpl
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater)

        if (!isLocationPermissionGranted())
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 100)

        viewModel.weatherData.observe(this) {
            Log.d("MainActivity", "data: $it")
            binding.tvWeather.text = it.toString()
        }

        viewModel.latLong.observe(this) { locationState ->
            Log.d("MainActivity", "location state: $locationState")
            viewModel.handleLocationState(locationState)
        }

        setContentView(binding.root)
    }

    override fun onResume() {
        super.onResume()
        viewModel.checkLocationPermission(isLocationPermissionGranted())

        viewModel.isLocationPermissionGranted.observe(this){ isGranted ->
            if (isGranted) lifecycleScope.launchWhenResumed {
                viewModel.requestCurrentLocationLatLong()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (permissions.isNotEmpty())
        handlePermissionDenials(requestCode, permissions, grantResults)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}



