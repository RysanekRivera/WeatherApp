package com.rysanek.weatherapp.data.remote.dtos

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all") val all: Int
)