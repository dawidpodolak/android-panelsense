package com.panelsense.core.model.panelconfig

import com.panelsense.core.model.mqqt.MqttConnConfig

data class SystemConfiguration (
    val mainPanelId: String?,
    val backgroundImageUrl: String? = null,
    val mqttConnConfig: MqttConnConfig?
)
