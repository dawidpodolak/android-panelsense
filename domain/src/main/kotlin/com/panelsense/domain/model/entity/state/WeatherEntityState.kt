package com.panelsense.domain.model.entity.state

class WeatherEntityState(
    override val entityId: String,
    val state: WeatherCondition? = null,
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
    val forecast: List<WeatherForecastEntity>? = null,
) : EntityState(entityId) {

    enum class WeatherCondition(val value: String) {
        CLEAR_NIGHT("clear-night"),
        CLOUDY("cloudy"),
        EXCEPTIONAL("exception"),
        FOG("fog"),
        HAIL("hail"),
        LIGHTNING("lightning"),
        LIGHTNING_RAINY("lightning-rainy"),
        PARTLY_CLOUDY("partlycloudy"),
        POURING("pouring"),
        RAINY("rainy"),
        SNOWY("snowy"),
        SNOWY_RAINY("snowy-rainy"),
        SUNNY("sunny"),
        WINDY("windy"),
        WINDY_VARIANT("windy-variant"),
    }

    data class WeatherForecastEntity(
        val condition: WeatherCondition? = null,
        val datetime: String? = null,
        val windBearing: Float? = null,
        val temperature: Float? = null,
        val templow: Float? = null,
        val windSpeed: Float? = null,
        val humidity: Float? = null
    )
}
