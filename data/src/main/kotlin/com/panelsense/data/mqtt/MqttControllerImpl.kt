package com.panelsense.data.mqtt

import com.panelsense.core.model.mqqt.MqttConnConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import org.eclipse.paho.mqttv5.client.IMqttToken
import org.eclipse.paho.mqttv5.client.MqttCallback
import org.eclipse.paho.mqttv5.client.MqttClient
import org.eclipse.paho.mqttv5.client.MqttConnectionOptions
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse
import org.eclipse.paho.mqttv5.client.persist.MemoryPersistence
import org.eclipse.paho.mqttv5.common.MqttException
import org.eclipse.paho.mqttv5.common.MqttMessage
import org.eclipse.paho.mqttv5.common.packet.MqttProperties
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import com.panelsense.core.model.mqqt.MqttMessage as NSMqttMessage

@Singleton
class MqttControllerImpl @Inject constructor(): MqttController, MqttCallback {

    override val messageFlow: MutableSharedFlow<NSMqttMessage> = MutableSharedFlow(replay = 1)
    lateinit var mqttClient: MqttClient
    override suspend fun connect(connConfig: MqttConnConfig) = withContext(Dispatchers.IO) {
        Timber.d("Connecting to MQTT broker: $connConfig")
        val memoryPersistence = MemoryPersistence()
        mqttClient = MqttClient(connConfig.address, connConfig.clientName, memoryPersistence)

        val connOptions = MqttConnectionOptions()
        connOptions.userName = connConfig.user
        connOptions.password = connConfig.password.toByteArray()
        connOptions.isCleanStart = true

        mqttClient.connect(connOptions)

        if (mqttClient.isConnected) {
            mqttClient.setCallback(this@MqttControllerImpl)
        }
        Timber.d("Connected to MQTT broker: ${mqttClient.isConnected}")
    }

    override suspend fun subscribeToTheTopic(topic: String) {
        withContext(Dispatchers.IO) {
            mqttClient.subscribe(topic, 2)
        }
    }

    override suspend fun publishMessage(topic: String, message: String) {
        withContext(Dispatchers.IO) {
            mqttClient.publish(topic, message.toByteArray(), 2, false)
        }
    }
    suspend fun unsubscribeFromTheTopic(topic: String) = withContext(Dispatchers.IO) {
        checkMqttConnection()
        mqttClient.unsubscribe(arrayOf(topic))
    }

    private fun checkMqttConnection() {
        if (!mqttClient.isConnected) throw IllegalStateException("MQTT client is not connected")
    }

    override suspend fun disconnect() = withContext(Dispatchers.IO) {
        mqttClient.disconnect()
    }

    override fun disconnected(disconnectResponse: MqttDisconnectResponse?) {
        Timber.i("Disconnected from MQTT broker: $disconnectResponse")
    }

    override fun mqttErrorOccurred(exception: MqttException?) {
        Timber.w(exception)
    }

    override fun messageArrived(topic: String?, message: MqttMessage?) {
        Timber.d("Message arrived: topic: $topic, $message")
        val wasEmitted = messageFlow.tryEmit(NSMqttMessage(topic ?: "", message.toString()))
        Timber.d("Message was emitted: $wasEmitted")
    }

    override fun deliveryComplete(token: IMqttToken?) {
        Timber.i("Message delivered: $token")
    }

    override fun connectComplete(reconnect: Boolean, serverURI: String?) {
        Timber.i("Connected to MQTT broker: $serverURI")
    }

    override fun authPacketArrived(reasonCode: Int, properties: MqttProperties?) {
        Timber.d("Auth packet arrived: $reasonCode, $properties")
    }
}
