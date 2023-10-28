package com.panelsense.app.ui.main.panel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.panelsense.core.model.mqqt.MqttMessage
import com.panelsense.core.model.panelconfig.PanelConfiguration
import com.panelsense.core.model.state.PanelState
import kotlinx.coroutines.flow.Flow
import timber.log.Timber

@Composable
inline fun <reified T : PanelState> Flow<MqttMessage>.getState(panelItem: PanelConfiguration.PanelItem): MutableState<T> {
    val state = remember {
        val panelState: PanelState = PanelState.getDefault<T>()
        mutableStateOf<T>(panelState as T)
    }

    panelItem.mqttTopic?.receiverTopic?.let { receiverTopic ->

        LaunchedEffect(key1 = receiverTopic) {
            collect { mqttMessage ->
                Timber.d("Message arrived to button: topic: subscribed topic: $panelItem, ${mqttMessage.topic}, ${mqttMessage.message}")
                if (mqttMessage.topic != receiverTopic) return@collect
                val typeToken = object : TypeToken<T>() {}.type
                state.value = Gson().fromJson<T?>(mqttMessage.message, typeToken)
            }
        }
    }

    return state
}
