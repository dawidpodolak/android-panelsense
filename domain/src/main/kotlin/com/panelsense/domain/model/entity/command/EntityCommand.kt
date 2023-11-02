package com.panelsense.domain.model.entity.command

sealed class EntityCommand(@Transient open val entityId: String)
