package com.panelsense.data.model

import com.panelsense.data.model.state.CoverState
import com.panelsense.data.model.state.LightState
import com.panelsense.data.model.state.SwitchState
import com.panelsense.data.model.state.WeatherState
import com.panelsense.domain.model.Configuration

enum class MessageType(val value: String, val dataClass: Class<out Any>) {
    AUTH("auth", AuthResultModel::class.java),
    CONFIGURATION("configuration", Configuration::class.java),
    ERROR("error", Any::class.java),
    STATE_REQUEST("ha_state_request", Any::class.java),
    LIGHT("ha_action_light", LightState::class.java),
    COVER("ha_action_cover", CoverState::class.java),
    SWITCH("ha_action_switch", SwitchState::class.java),
    WEATHER("ha_action_weather", WeatherState::class.java)
}
