package com.panelsense.data.model.state

import com.panelsense.data.mapper.toEntityState
import com.panelsense.domain.model.entity.state.EntityState

data class CoverState(
    val entityId: String,
    val state: String?,
    val position: Int?,
    val tiltPosition: Int?,
    val icon: String?,
    val friendlyName: String?,
    val deviceClass: String?,
    val supportedFeatures: Int?
) : DataState {

    override fun toDomainState(): EntityState = toEntityState()
}
