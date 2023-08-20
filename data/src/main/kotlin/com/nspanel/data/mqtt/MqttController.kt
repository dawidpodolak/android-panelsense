package com.nspanel.data.mqtt

import com.nspanel.core.model.mqqt.MqttConnConfig
import com.nspanel.core.model.mqqt.MqttMessage
import kotlinx.coroutines.flow.SharedFlow

interface MqttController {

    val messageFlow: SharedFlow<MqttMessage>
    suspend fun connect(connConfig: MqttConnConfig)
    suspend fun disconnect()
    suspend fun subscribeToTheTopic(topic: String)
    suspend fun publishMessage(topic: String, message: String)
}
