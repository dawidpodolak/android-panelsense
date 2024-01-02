package com.panelsense.data.model.state

import com.panelsense.data.mapper.toEntityState
import com.panelsense.domain.model.entity.state.EntityState

data class SensorState(
    val entityId: String,
    val state: String,
    val stateClass: String?,
    val unitOfMeasurement: String?,
    val deviceClass: String?,
    val batteryLevel: String?,
    val friendlyName: String?,
    val icon: String?
) : DataState {

    override fun toDomainState(): EntityState = toEntityState()
}
