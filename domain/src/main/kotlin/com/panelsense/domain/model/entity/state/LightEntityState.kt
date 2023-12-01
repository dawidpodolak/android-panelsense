package com.panelsense.domain.model.entity.state

import com.panelsense.domain.model.entity.EntityFeature
import com.panelsense.domain.model.entity.command.BrightnessLightCommand
import com.panelsense.domain.model.entity.command.LightEntityCommand
import com.panelsense.domain.model.entity.command.RGBLightCommand
import com.panelsense.domain.model.entity.command.TemperatureLightCommand
import com.panelsense.domain.model.entity.command.ToggleLightCommand

data class LightEntityState(
    override val entityId: String,
    val on: Boolean,
    val brightness: Int?,
    val colorMode: ColorMode?,
    val rgbColor: Color?,
    val rgbwwColor: ColorWW?,
    val colorTempKelvin: Int?,
    val colorTempKelvinRange: ColorTempRange?,
    val supportedColorModes: List<ColorMode> = emptyList(),
    val friendlyName: String?,
    val icon: String?,
    val supportedFeatures: Set<Feature>
) : EntityState(entityId) {
    val hasFeatures: Boolean
        get() = supportedColorModes.isNotEmpty() || brightness != null

    enum class ColorMode {
        ON_OFF,
        BRIGHTNESS,
        COLOR_TEMP,
        HS,
        RGB,
        RGB_W,
        RGB_WW,
        WHITE,
        XY
    }

    data class Color(
        val red: Int,
        val green: Int,
        val blue: Int
    )

    data class ColorWW(
        val red: Int,
        val green: Int,
        val blue: Int,
        val warmWhite: Int,
        val coldWhite: Int,
    )

    data class ColorTempRange(
        val min: Int,
        val max: Int
    )


    @Suppress("MagicNumber")
    enum class Feature(override val value: Int) : EntityFeature {
        BRIGHTNESS(1),
        COLOR_TEMP(2),
        EFFECT(4),
        FLASH(8),
        COLOR(16),
        TRANSITION(32),
        WHITE_VALUE(128),
    }

    fun getToggleCommand(): LightEntityCommand =
        ToggleLightCommand(
            entityId = entityId,
            on = !on
        )

    fun getBrightnessCommand(toInt: Int): BrightnessLightCommand = BrightnessLightCommand(
        entityId = entityId,
        brightness = toInt
    )

    fun getRGBCommand(red: Int, green: Int, blue: Int): RGBLightCommand = RGBLightCommand(
        entityId = entityId,
        on = true,
        rgbColor = arrayOf(red, green, blue).toIntArray()
    )

    fun getTempCommand(temp: Int): TemperatureLightCommand = TemperatureLightCommand(
        entityId = entityId,
        on = true,
        colorTempKelvin = temp
    )
}
