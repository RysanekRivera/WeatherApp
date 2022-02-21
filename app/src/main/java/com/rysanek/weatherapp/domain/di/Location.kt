package com.rysanek.weatherapp.domain.di

import android.app.Application
import com.rysanek.weatherapp.BuildConfig
import com.rysanek.weatherapp.domain.location.PlacesApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Location {

    @Singleton
    @Provides
    @Named("locationApiKey")
    fun provideLocationApiKey(): String = BuildConfig.PLACES_API_KEY

    @Singleton
    @Provides
    fun providePlacesApi(
        app: Application,
        @Named("locationApiKey") locationApiKey: String
    ): PlacesApi = PlacesApi(app, locationApiKey)

}