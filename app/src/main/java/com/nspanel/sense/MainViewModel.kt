package com.nspanel.sense

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nspanel.core.model.panelconfig.PanelConfiguration
import com.nspanel.core.model.panelconfig.PanelConfiguration.PanelItem
import com.nspanel.core.model.panelconfig.SenseConfiguration
import com.nspanel.core.model.state.PanelState
import com.nspanel.data.mqtt.MqttController
import com.nspanel.data.repository.ConfigurationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val configRepository: ConfigurationRepository,
    private val mqttController: MqttController
) : ViewModel() {

    private val _stateFlow = MutableStateFlow(MainViewState())
    val stateFlow: StateFlow<MainViewState> = _stateFlow
    val messageFlow = mqttController.messageFlow

    init {
        loadConfiguration()
    }

    private fun loadConfiguration() {
        viewModelScope.launch {
            val senseConfig = configRepository.getConfiguration()
            Timber.d("Sense config: $senseConfig")
            _stateFlow.value = MainViewState(senseConfig)
            mqttSetup(senseConfig)
        }
    }

    private suspend fun mqttSetup(senseConfig: SenseConfiguration?) {
        runCatching {
            val mqttConnConfig =
                senseConfig?.systemConfiguration?.mqttConnConfig ?: return@runCatching
            mqttController.connect(mqttConnConfig)

            subscribeToPanelMqttTopics(senseConfig)
        }.onFailure { Timber.e(it) }
    }

    private suspend fun subscribeToPanelMqttTopics(senseConfig: SenseConfiguration) {
        senseConfig
            .panelList
            .flatMap {
                when (it) {
                    is PanelConfiguration.HomePanel -> it.homeItems
                    is PanelConfiguration.GridPanel -> it.gridItems
                }
            }
            .mapNotNull { it.mqttTopic?.receiverTopic }
            .forEach { topic ->
                mqttController.subscribeToTheTopic(topic)
            }
    }

    override fun onCleared() {
        viewModelScope.launch {
            mqttController.disconnect()
        }
    }

    fun onItemClick(item: PanelItem) {
        viewModelScope.launch {
            when (item) {
                is PanelItem.ButtonItem -> {
                    item.mqttTopic?.publishTopic?.let { publishTopic ->
                        val state = when (item.state.state) {
                            PanelState.ButtonState.State.ON -> "OFF"
                            PanelState.ButtonState.State.OFF -> "ON"
                        }
                        mqttController.publishMessage(publishTopic, state)
                    }
                }
            }
        }
    }

    data class MainViewState(
        val senseConfiguration: SenseConfiguration? = null
    )
}
