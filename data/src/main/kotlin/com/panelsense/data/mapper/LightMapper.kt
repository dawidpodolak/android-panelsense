package com.panelsense.data.mapper

import com.panelsense.data.model.state.LightState
import com.panelsense.domain.model.entity.state.LightEntityState

@Suppress("MagicNumber")
fun LightState.toEntityState(): LightEntityState = LightEntityState(
    entityId = entityId,
    on = on,
    brightness = brightness,
    colorMode = colorMode.toColorMode(),
    rgbColor = rgbColor?.let { LightEntityState.Color(it[0], it[1], it[2]) },
    rgbwwColor = rgbwwColor?.let { LightEntityState.ColorWW(it[0], it[1], it[2], it[3], it[4]) },
    colorTempKelvin = colorTempKelvin,
    colorTempKelvinRange = if (minColorTempKelvin != null && maxColorTempKelvin != null) {
        LightEntityState.ColorTempRange(
            minColorTempKelvin,
            maxColorTempKelvin
        )
    } else null,
    supportedColorModes = supportedColorModes?.mapNotNull { it.toColorMode() } ?: emptyList(),
    friendlyName = friendlyName,
    icon = icon,
    supportedFeatures = supportedFeatures.toSupportedFeature(LightEntityState.Feature.values()),
)

private fun String?.toColorMode(): LightEntityState.ColorMode? = when (this) {
    "onff" -> LightEntityState.ColorMode.ON_OFF
    "brightness" -> LightEntityState.ColorMode.BRIGHTNESS
    "color_temp" -> LightEntityState.ColorMode.COLOR_TEMP
    "hs" -> LightEntityState.ColorMode.HS
    "rgb" -> LightEntityState.ColorMode.RGB
    "rgbw" -> LightEntityState.ColorMode.RGB_W
    "rgbww" -> LightEntityState.ColorMode.RGB_WW
    "white" -> LightEntityState.ColorMode.WHITE
    "xy" -> LightEntityState.ColorMode.XY
    else -> null
}
