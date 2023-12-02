package com.panelsense.domain.model.entity.command

data class CoverEntityCommand(
    override val entityId: String,
    val state: String
) : EntityCommand(entityId)

data class CoverPositionEntityCommand(
    override val entityId: String,
    val position: Int
) : EntityCommand(entityId)
