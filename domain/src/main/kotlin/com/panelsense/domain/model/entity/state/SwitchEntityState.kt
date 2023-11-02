package com.panelsense.domain.model.entity.state

data class SwitchEntityState(
    override val entityId: String,
    val on: Boolean = false,
    val friendlyName: String?,
    val icon: String?
) : EntityState(entityId)
