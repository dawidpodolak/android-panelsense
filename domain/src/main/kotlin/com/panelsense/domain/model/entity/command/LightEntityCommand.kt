package com.panelsense.domain.model.entity.command

sealed class LightEntityCommand(@Transient override val entityId: String) : EntityCommand(entityId)

data class ToggleLightCommand(
    override val entityId: String,
    val on: Boolean
) : LightEntityCommand(entityId)

data class BrightnessLightCommand(
    override val entityId: String,
    val on: Boolean = true,
    val brightness: Int
) : LightEntityCommand(entityId)
