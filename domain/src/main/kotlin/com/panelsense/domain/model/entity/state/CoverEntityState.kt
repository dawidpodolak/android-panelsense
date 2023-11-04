package com.panelsense.domain.model.entity.state

class CoverEntityState(
    override val entityId: String,
    val state: State,
    val position: Int?,
    val tiltPosition: Int?,
    val icon: String?,
    val friendlyName: String?,
    val deviceClass: DeviceClass?,
    val supportedFeatures: Set<SupportedFeatures> = emptySet()
) : EntityState(entityId) {
    enum class DeviceClass {
        AWNING,
        BLIND,
        CURTAIN,
        DAMPER,
        DOOR,
        GARAGE,
        GATE,
        SHADE,
        SHUTTER,
        WINDOW,
    }

    enum class State {
        OPEN,
        CLOSED,
        OPENING,
        CLOSING,
        STOPPED,
    }

    enum class SupportedFeatures(val value: Int) {
        OPEN(1),
        CLOSE(2),
        SET_POSITION(4),
        STOP(8),
        OPEN_TILT(16),
        CLOSE_TILT(32),
        STOP_TILT(64),
        SET_TILT_POSITION(128)
    }
}
