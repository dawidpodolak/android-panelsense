package com.panelsense.data.model.state

class WeatherState(
    val entityId: String,
    val state: String? = null,
    val temperature: Float? = null,
    val dewPoint: Float? = null,
    val temperatureUnit: String? = null,
    val humidity: Float? = null,
    val cloudCoverage: Float? = null,
    val pressure: Float? = null,
    val pressureUnit: String? = null,
    val windBearing: Float? = null,
    val windSpeedUnit: String? = null,
    val visibilityUnit: String? = null,
    val precipitationUnit: String? = null,
    val forecast: List<WeatherForecast>? = null,
) {

    data class WeatherForecast(
        val condition: String? = null,
        val datetime: String? = null,
        val windBearing: Float? = null,
        val temperature: Float? = null,
        val templow: Float? = null,
        val windSpeed: Float? = null,
        val humidity: Float? = null
    )
}
