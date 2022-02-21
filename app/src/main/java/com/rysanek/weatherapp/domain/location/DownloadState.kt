package com.rysanek.weatherapp.domain.location

sealed class DownloadState{
    object Idle: DownloadState()
    object Downloading: DownloadState()
    data class Success<T>(val data: T): DownloadState()
    data class Error(val message: String): DownloadState()
}