package com.panelsense.data.mapper

import com.panelsense.data.model.state.SensorState
import com.panelsense.domain.model.entity.state.SensorEntityState

fun SensorState.toEntityState(): SensorEntityState =
    SensorEntityState(
        entityId = entityId,
        state = state,
        stateClass = runCatching { stateClass?.let { SensorEntityState.StateClass.valueOf(stateClass.uppercase()) } }.getOrNull(),
        unitOfMeasurement = unitOfMeasurement,
        deviceClass = runCatching {
            deviceClass?.let {
                SensorEntityState.DeviceClass.values().firstOrNull { it.className == deviceClass }
            }
        }.getOrNull(),
        batteryLevel = batteryLevel,
        friendlyName = friendlyName,
        icon = icon?.removePrefix("mdi:")
    )
