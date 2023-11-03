package com.panelsense.data.mapper

import com.panelsense.data.model.state.WeatherState
import com.panelsense.domain.model.entity.state.WeatherEntityState

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
        datetime = datetime,
        windBearing = windBearing,
        temperature = temperature,
        templow = templow,
        windSpeed = windSpeed,
        humidity = humidity
    )

private fun String.toWeatherState(): WeatherEntityState.WeatherCondition =
    WeatherEntityState.WeatherCondition.values().firstOrNull { it.value == this }
        ?: WeatherEntityState.WeatherCondition.EXCEPTIONAL
