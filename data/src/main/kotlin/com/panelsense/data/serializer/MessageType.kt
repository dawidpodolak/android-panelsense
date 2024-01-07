package com.panelsense.data.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import com.panelsense.data.model.MessageType
import java.lang.reflect.Type

class MessageTypeSerializer : JsonSerializer<MessageType> {
    override fun serialize(
        src: MessageType?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement = JsonPrimitive(src?.value)
}

class MessageTypeDeserializer : JsonDeserializer<MessageType> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): MessageType =
        MessageType.values().firstOrNull { it.value == json?.asString } ?: MessageType.UNSUPPORTED
}
