package com.panelsense.app.ui.main

import androidx.lifecycle.viewModelScope
import com.panelsense.core.base.NavViewModel
import com.panelsense.core.model.panelconfig.PanelConfiguration
import com.panelsense.core.model.panelconfig.PanelConfiguration.PanelItem
import com.panelsense.core.model.panelconfig.SenseConfiguration
import com.panelsense.core.model.state.PanelState
import com.panelsense.data.icons.IconProvider
import com.panelsense.data.mqtt.MqttController
import com.panelsense.data.repository.ConfigurationRepository
import com.panelsense.domain.interactor.LoginInteractor
import com.panelsense.domain.interactor.PanelSenseInteractor
import com.panelsense.domain.model.Configuration
import com.panelsense.domain.model.ConnectionState
import com.panelsense.domain.model.entity.command.EntityCommand
import com.panelsense.domain.model.entity.state.EntityState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import kotlin.reflect.KClass

@HiltViewModel
class MainViewModel @Inject constructor(
    private val configRepository: ConfigurationRepository,
    private val mqttController: MqttController,
    private val loginInteractor: LoginInteractor,
    private val panelSenseInteractor: PanelSenseInteractor,
    private var iconProvider: IconProvider
) : NavViewModel<MainNavCommand, MainViewModel.MainViewState>(), EntityInteractor {

    override fun defaultState(): MainViewState = MainViewState()
    val messageFlow = mqttController.messageFlow

    init {
        loadConfiguration()
        checkIfUserIsLoggedIn()
        observeConnection()
        observeConfiguration()
    }

    private fun checkIfUserIsLoggedIn() {
        viewModelScope.launch {
            if (!loginInteractor.isUserLoggedIn()) {
                navigateTo(MainNavCommand.NavigateToLogin)
                return@launch
            }

            val result = loginInteractor.relogin()
            if (result.isFailure) {
                navigateTo(MainNavCommand.NavigateToLogin)
            }
        }
    }

    private fun loadConfiguration() {
        viewModelScope.launch {
            val senseConfig = configRepository.getConfiguration()
            modify { copy(senseConfiguration = senseConfig) }
            mqttSetup(senseConfig)
        }
    }

    private fun observeConnection() {
        viewModelScope.launch {
            panelSenseInteractor.connectionState().collect { state ->
                modify { copy(serverConnected = state == ConnectionState.CONNECTED) }
            }
        }
    }

    private fun observeConfiguration() {
        viewModelScope.launch {
            panelSenseInteractor.configuration().collect { config ->
                modify { copy(panelConfiguration = config) }
            }
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

    override fun <T : EntityState> listenOnState(entityId: String, kType: KClass<T>): Flow<T> {
        return panelSenseInteractor.observeEntityState(entityId)
            .mapNotNull { it as? T }

    }

    override fun sendCommand(command: EntityCommand) {
        panelSenseInteractor.sendCommand(command)
    }

    override fun getIconProvider(): IconProvider = iconProvider

    data class MainViewState(
        @Deprecated("Use panelConfiguration")
        val senseConfiguration: SenseConfiguration? = null,
        val panelConfiguration: Configuration? = null,
        val serverConnected: Boolean = true
    )
}
