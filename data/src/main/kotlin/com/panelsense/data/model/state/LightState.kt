package com.panelsense.data.model.state

data class LightState(
    val entityId: String,
    val on: Boolean,
    val brightness: Int?,
    val colorMode: String?,
    val rgbColor: List<Int>?,
    val rgbwwColor: List<Int>?,
    val colorTempKelvin: Int?,
    val maxColorTempKelvin: Int?,
    val minColorTempKelvin: Int?,
    val supportedColorModes: List<String>?,
    val friendlyName: String?,
    val icon: String?,
    val supportedFeatures: Int?
)
