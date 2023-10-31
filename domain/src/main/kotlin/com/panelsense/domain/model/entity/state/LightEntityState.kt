package com.panelsense.domain.model.entity.state

data class LightEntityState(
    override val entityId: String,
    val on: Boolean,
    val brightness: Int?,
    val colorMode: ColorMode?,
    val rgbColor: Color?,
    val colorTempKelvin: Int?,
    val colorTempKelvinRange: ColorTempRange?,
    val supportedColorModes: List<ColorMode> = emptyList(),
    val friendlyName: String?,
    val icon: String?
) : EntityState(entityId) {
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

    data class ColorTempRange(
        val min: Int,
        val max: Int
    )
}
