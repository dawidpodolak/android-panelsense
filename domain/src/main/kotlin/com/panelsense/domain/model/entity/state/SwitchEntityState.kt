package com.panelsense.domain.model.entity.state

import com.panelsense.domain.model.entity.command.ToggleSwitchCommand

data class SwitchEntityState(
    override val entityId: String,
    val on: Boolean = false,
    val friendlyName: String?,
    val icon: String?
) : EntityState(entityId) {

    fun getToggleCommand(): ToggleSwitchCommand =
        ToggleSwitchCommand(
            entityId = entityId,
            on = !on
        )
}
