package com.panelsense.core.model.mqqt

data class MqttMessage(
    val topic: String,
    val message: String
)
