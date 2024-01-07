package com.panelsense.data.model.state

import com.panelsense.data.mapper.toEntityState
import com.panelsense.domain.model.entity.state.EntityState

data class BinarySensorState(
    val entityId: String,
    val state: String,
    val deviceClass: String?,
    val friendlyName: String?,
    val icon: String?
) : DataState {

    override fun toDomainState(): EntityState = toEntityState()
}
