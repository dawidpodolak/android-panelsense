package com.panelsense.domain.model.entity.command

class CoverEntityCommand(
    override val entityId: String,
    val state: String
) : EntityCommand(entityId)
