package com.panelsense.data.serializer

import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.panelsense.domain.model.Panel
import com.panelsense.domain.model.PanelType
import java.lang.reflect.Type


class PanelTypeDeserializer : JsonDeserializer<PanelType> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): PanelType {
        return PanelType.values().first { it.type == json.asString }
    }
}

class PanelDeserializer : JsonDeserializer<Panel> {
    override fun deserialize(
        json: JsonElement,
        typeOfT: Type,
        context: JsonDeserializationContext
    ): Panel {
        val jsonPanelType = json?.asJsonObject?.get("type")?.asString
        val type = PanelType.values().firstOrNull { it.type == jsonPanelType }
        return when (type) {
            PanelType.HOME -> context.deserialize<Panel.HomePanel>(
                json,
                Panel.HomePanel::class.java
            )

            PanelType.GRID -> context.deserialize<Panel.HomePanel>(
                json,
                Panel.GridPanel::class.java
            )

            PanelType.FLEX -> context.deserialize<Panel.FlexPanel>(
                json,
                Panel.FlexPanel::class.java
            )

            else -> Panel.UnknownPanel()
        }
    }
}
