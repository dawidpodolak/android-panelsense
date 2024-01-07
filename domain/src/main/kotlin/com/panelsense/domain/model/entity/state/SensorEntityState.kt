package com.panelsense.domain.model.entity.state

import kotlin.math.roundToInt

data class SensorEntityState(
    override val entityId: String,
    val state: String,
    val stateClass: StateClass?,
    val unitOfMeasurement: String?,
    val deviceClass: DeviceClass?,
    val batteryLevel: String?,
    val friendlyName: String?,
    val icon: String?
) : EntityState(entityId) {

    val mdiIcon: String?
        get() = when {
            deviceClass == DeviceClass.BATTERY -> getBatteryMdiIcon()
            else -> deviceClass?.icon
        }

    private fun getBatteryMdiIcon(): String {
        return batteryLevel?.toFloatOrNull()?.div(10f)?.roundToInt()?.times(10).run {
            "battery-${this ?: 0}"
        }
    }

    @Suppress("StringLiteralDuplication")
    enum class DeviceClass(val className: String, val icon: String) {
        APPARENT_POWER("apparent_power", "flash"),
        AQI("aqi", "air-filter"),
        ATMOSPHERIC_PRESSURE("atmospheric_pressure", "thermometer-lines"),
        BATTERY("battery", ""), // battery icon is calculated in getBatteryMdiIcon()
        CO2("carbon_dioxide", "molecule-co2"),
        CO("carbon_monoxide", "molecule-co"),
        CURRENT("current", "current-ac"),
        DATA_RATE("data_rate", "transmission-tower"),
        DATA_SIZE("data_size", "database"),
        DATE("date", "calendar"),
        DISTANCE("distance", "arrow-left-right"),
        DURATION("duration", "progress-clock"),
        ENERGY("energy", "lightning-bolt"),
        ENERGY_STORAGE("energy_storage", "home-lightning-bolt"),
        FREQUENCY("frequency", "sine-wave"),
        GAS("gas", "meter-gas"),
        HUMIDITY("humidity", "water-percent"),
        ILLUMINANCE("illuminance", "brightness"),
        IRRADIANCE("irradiance", "sun-wireless"),
        MOISTURE("moisture", "water-percent"),
        MONETARY("monetary", "cash"),
        NITROGEN_DIOXIDE("nitrogen_dioxide", "molecule"),
        NITROGEN_MONOXIDE("nitrogen_monoxide", "molecule"),
        NITROUS_OXIDE("nitrous_oxide", "molecule"),
        OZONE("ozone", "molecule"),
        PH("ph", "ph"),
        PM1("pm1", "molecule"),
        PM25("pm10", "molecule"),
        PM10("pm25", "molecule"),
        POWER("power", "flash"),
        POWER_FACTOR("power_factor", "angle-acute"),
        PRECIPITATION("precipitation", "weather-rainy"),
        PRECIPITATION_INTENSITY("precipitation_intensity", "eather-pouring"),
        PRESSURE("pressure", "gauge"),
        REACTIVE_POWER("reactive_power", "flash"),
        SIGNAL_STRENGTH("signal_strength", "wifi"),
        SOUND_PRESSURE("sound_pressure", "ear-hearing"),
        SPEED("speed", "speedometer"),
        SULPHUR_DIOXIDE("sulphur_dioxide", "molecule"),
        TEMPERATURE("temperature", "thermometer"),
        TIMESTAMP("timestamp", "clock"),
        VOLATILE_ORGANIC_COMPOUNDS("volatile_organic_compounds", "molecule"),
        VOLATILE_ORGANIC_COMPOUNDS_PARTS("volatile_organic_compounds_parts", "molecule"),
        VOLTAGE("voltage", "sine-wave"),
        VOLUME("volume", "car-coolant-evel"),
        VOLUME_STORAGE("volume", "car-coolant-level"),
        WATER("water", "water"),
        WEIGHT("weight", "weight"),
        WIND_SPEED("wind_speed", "weather-windy"),
    }

    enum class StateClass(val icon: String) {
        MEASUREMENT(""),
        TOTAL_INCREASING(""),
        TOTAL(""),
    }
}
