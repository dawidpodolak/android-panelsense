package com.panelsense.data.model

import com.google.gson.JsonElement

data class WebsocketModel(
    val type: MessageType,
    val data: JsonElement
)
