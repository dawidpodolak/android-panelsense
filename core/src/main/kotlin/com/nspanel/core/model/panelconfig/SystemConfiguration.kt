package com.nspanel.core.model.panelconfig

import com.nspanel.core.model.mqqt.MqttConnConfig

data class SystemConfiguration (
    val mainPanelId: String?,
    val backgroundImageUrl: String? = null,
    val mqttConnConfig: MqttConnConfig?
)
