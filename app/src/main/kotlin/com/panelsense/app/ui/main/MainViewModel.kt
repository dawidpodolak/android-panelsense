package com.panelsense.app.ui.main

import androidx.lifecycle.viewModelScope
import com.panelsense.core.base.NavViewModel
import com.panelsense.core.model.panelconfig.PanelConfiguration
import com.panelsense.core.model.panelconfig.PanelConfiguration.PanelItem
import com.panelsense.core.model.panelconfig.SenseConfiguration
import com.panelsense.core.model.state.PanelState
import com.panelsense.data.mqtt.MqttController
import com.panelsense.data.repository.ConfigurationRepository
import com.panelsense.domain.interactor.LoginInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val configRepository: ConfigurationRepository,
    private val mqttController: MqttController,
    private val loginInteractor: LoginInteractor
) : NavViewModel<MainNavCommand>() {

    private val _stateFlow = MutableStateFlow(MainViewState())
    val stateFlow: StateFlow<MainViewState> = _stateFlow
    val messageFlow = mqttController.messageFlow

    init {
        loadConfiguration()
        checkIfUserIsLoggedIn()
    }

    private fun checkIfUserIsLoggedIn() {
        viewModelScope.launch {
            if (!loginInteractor.isUserLoggedIn()) {
                navigateTo(MainNavCommand.NavigateToLogin)
            }
        }
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
