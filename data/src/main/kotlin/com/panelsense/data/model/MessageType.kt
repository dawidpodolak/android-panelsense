package com.panelsense.data.model

import com.panelsense.domain.model.Configuration

enum class MessageType(val value: String, val dataClass: Class<out Any>) {
    AUTH("auth", AuthResultModel::class.java),
    CONFIGURATION("configuration", Configuration::class.java),
    ERROR("error", Any::class.java),
    LIGHT("ha_action_light", Any::class.java),
    COVER("ha_action_cover", Any::class.java),
    SWITCH("ha_action_switch", Any::class.java)
}
