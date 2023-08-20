package com.nspanel.core.model.mqqt

data class MqttConnConfig(
    val address: String,
    val port: Int,
    val clientName: String,
    val user: String,
    val password: String,
)
