package com.panelsense.data.model.state

import com.panelsense.data.mapper.toEntityState
import com.panelsense.domain.model.entity.state.EntityState

data class WeatherState(
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
    val windSpeed: Float? = null,
    val windSpeedUnit: String? = null,
    val visibilityUnit: String? = null,
    val precipitationUnit: String? = null,
    val forecast: List<WeatherForecast>? = null,
) : DataState {

    override fun toDomainState(): EntityState = toEntityState()

    data class WeatherForecast(
        val condition: String? = null,
        val datetime: String? = null,
        val windBearing: Float? = null,
        val temperature: Float? = null,
        val templow: Float? = null,
        val windSpeed: Float? = null,
        val humidity: Float? = null,
        val pressure: Float? = null
    )
}
