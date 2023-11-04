package com.panelsense.data.mapper

import com.panelsense.data.model.state.CoverState
import com.panelsense.domain.model.entity.state.CoverEntityState
import com.panelsense.domain.model.entity.state.CoverEntityState.State
import com.panelsense.domain.model.entity.state.CoverEntityState.SupportedFeatures

fun CoverState.toEntityState(): CoverEntityState =
    CoverEntityState(
        entityId = entityId,
        state = state.toCoverState(),
        position = position,
        tiltPosition = tiltPosition,
        icon = icon,
        friendlyName = friendlyName,
        deviceClass = deviceClass.toDeviceClass(),
        supportedFeatures = supportedFeatures.toSupportedFeature(),
    )

private fun String?.toCoverState(): State =
    when (this) {
        "open" -> State.OPEN
        "closed" -> State.CLOSED
        "opening" -> State.OPENING
        "closing" -> State.CLOSING
        "stopped" -> State.STOPPED
        else -> State.OPEN
    }

private fun String?.toDeviceClass(): CoverEntityState.DeviceClass =
    when (this) {
        "awning" -> CoverEntityState.DeviceClass.AWNING
        "blind" -> CoverEntityState.DeviceClass.BLIND
        "curtain" -> CoverEntityState.DeviceClass.CURTAIN
        "damper" -> CoverEntityState.DeviceClass.DAMPER
        "door" -> CoverEntityState.DeviceClass.DOOR
        "garage" -> CoverEntityState.DeviceClass.GARAGE
        "gate" -> CoverEntityState.DeviceClass.GATE
        "shade" -> CoverEntityState.DeviceClass.SHADE
        "shutter" -> CoverEntityState.DeviceClass.SHUTTER
        "window" -> CoverEntityState.DeviceClass.WINDOW
        else -> CoverEntityState.DeviceClass.SHUTTER
    }

private fun Int?.toSupportedFeature(): Set<SupportedFeatures> {
    if (this == null) return emptySet()
    val supportedFeatures = mutableSetOf<SupportedFeatures>()
    SupportedFeatures.values().forEach {
        if (this.and(it.value) == it.value) {
            supportedFeatures.add(it)
        }
    }
    return supportedFeatures
}
