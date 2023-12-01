package com.panelsense.data.mapper

import com.panelsense.data.model.state.WeatherState
import com.panelsense.domain.model.entity.state.WeatherEntityState
import org.threeten.bp.ZonedDateTime

fun WeatherState.toEntityState(): WeatherEntityState =
    WeatherEntityState(
        entityId = entityId,
        state = state?.toWeatherState(),
        temperature = temperature,
        dewPoint = dewPoint,
        temperatureUnit = temperatureUnit,
        humidity = humidity,
        cloudCoverage = cloudCoverage,
        pressure = pressure,
        pressureUnit = pressureUnit,
        windBearing = windBearing,
        windSpeed = windSpeed,
        windSpeedUnit = windSpeedUnit,
        visibilityUnit = visibilityUnit,
        precipitationUnit = precipitationUnit,
        forecast = forecast?.map { it.toEntityState() },
    )

private fun WeatherState.WeatherForecast.toEntityState(): WeatherEntityState.WeatherForecastEntity =
    WeatherEntityState.WeatherForecastEntity(
        condition = condition?.toWeatherState(),
        datetime = datetime?.let { ZonedDateTime.parse(it) },
        windBearing = windBearing,
        temperature = temperature,
        templow = templow,
        windSpeed = windSpeed,
        humidity = humidity,
        pressure = pressure
    )

private fun String.toWeatherState(): WeatherEntityState.WeatherCondition =
    WeatherEntityState.WeatherCondition.values().firstOrNull { it.value == this }
        ?: WeatherEntityState.WeatherCondition.EXCEPTIONAL
