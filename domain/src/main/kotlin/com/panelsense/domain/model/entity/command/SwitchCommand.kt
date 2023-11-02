package com.panelsense.domain.model.entity.command

data class ToggleSwitchCommand(
    override val entityId: String,
    val on: Boolean
) : EntityCommand(entityId)
