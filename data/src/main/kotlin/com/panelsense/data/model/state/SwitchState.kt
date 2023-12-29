package com.panelsense.data.model.state

import com.panelsense.data.mapper.toEntityState
import com.panelsense.domain.model.entity.state.EntityState

data class SwitchState(
    val entityId: String,
    val on: Boolean,
    val friendlyName: String?,
    val icon: String?
) : DataState {

    override fun toDomainState(): EntityState = toEntityState()
}
