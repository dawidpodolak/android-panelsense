package com.panelsense.domain.model.entity.state

import com.panelsense.domain.model.entity.EntityFeature
import com.panelsense.domain.model.entity.command.CoverEntityCommand
import com.panelsense.domain.model.entity.command.CoverPositionEntityCommand
import com.panelsense.domain.model.entity.command.EntityCommand

data class CoverEntityState(
    override val entityId: String,
    val state: State,
    val position: Int?,
    val tiltPosition: Int?,
    val icon: String?,
    val friendlyName: String?,
    val deviceClass: DeviceClass?,
    val supportedFeatures: Set<Feature> = emptySet()
) : EntityState(entityId) {

    fun getOpenCoverCommand(): EntityCommand =
        CoverEntityCommand(
            entityId = entityId,
            state = "open"
        )

    fun getCloseCoverCommand(): EntityCommand =
        CoverEntityCommand(
            entityId = entityId,
            state = "close"
        )

    fun getPositionCoverCommand(position: Int): EntityCommand =
        CoverPositionEntityCommand(
            entityId = entityId,
            position = position
        )

    fun getStopCoverCommand(): EntityCommand =
        CoverEntityCommand(
            entityId = entityId,
            state = "stop"
        )

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

    @Suppress("MagicNumber")
    enum class Feature(override val value: Int) : EntityFeature {
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
