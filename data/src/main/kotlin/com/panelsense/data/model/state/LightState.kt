package com.panelsense.data.model.state

import com.panelsense.data.mapper.toEntityState
import com.panelsense.domain.model.entity.state.EntityState

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
) : DataState {

    override fun toDomainState(): EntityState = toEntityState()
}
