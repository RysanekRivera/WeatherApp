package com.rysanek.weatherapp.domain.di

import com.rysanek.weatherapp.data.remote.apis.WeatherApi
import com.rysanek.weatherapp.data.repositories.WeatherRepositoryImpl
import com.rysanek.weatherapp.domain.utils.Constants.WEATHER_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Network {

    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient = OkHttpClient()
        .newBuilder()
        .addNetworkInterceptor(HttpLoggingInterceptor().also { interceptor ->
            interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        }).build()

    @Singleton
    @Provides
    fun provideWeatherApi(
        okHttpClient: OkHttpClient
    ): WeatherApi = Retrofit.Builder()
        .baseUrl(WEATHER_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()
        .create(WeatherApi::class.java)

    @Singleton
    @Provides
    fun provideWeatherRepository(
        api: WeatherApi
    ): WeatherRepositoryImpl = WeatherRepositoryImpl(api)

}