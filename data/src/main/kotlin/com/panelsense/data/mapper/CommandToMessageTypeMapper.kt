package com.panelsense.data.mapper

import com.google.gson.Gson
import com.panelsense.data.model.MessageType
import com.panelsense.data.model.WebsocketModel
import com.panelsense.domain.model.EntityDomain
import com.panelsense.domain.model.entity.command.EntityCommand
import com.panelsense.domain.toDomain

fun EntityCommand.toWebsocketModel(gson: Gson): WebsocketModel? {
    val messageType = this.toMessageType() ?: return null
    return WebsocketModel(
        type = messageType,
        data = gson.toJsonTree(this)
    )
}

fun EntityCommand.toMessageType(): MessageType? = when (this.entityId.toDomain) {
    EntityDomain.SWITCH -> MessageType.SWITCH
    EntityDomain.LIGHT -> MessageType.LIGHT
    else -> null
}
