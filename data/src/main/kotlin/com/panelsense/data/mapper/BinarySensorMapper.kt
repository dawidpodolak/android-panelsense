package com.panelsense.data.mapper

import com.panelsense.data.model.state.BinarySensorState
import com.panelsense.domain.model.entity.state.BinarySensorEntityState

fun BinarySensorState.toEntityState(): BinarySensorEntityState =
    BinarySensorEntityState(
        entityId = entityId,
        state = state == "on",
        deviceClass = runCatching {
            deviceClass?.let {
                BinarySensorEntityState.DeviceClass.valueOf(
                    deviceClass.uppercase()
                )
            }
        }.getOrNull(),
        friendlyName = friendlyName,
        icon = icon?.removePrefix("mdi:")
    )
