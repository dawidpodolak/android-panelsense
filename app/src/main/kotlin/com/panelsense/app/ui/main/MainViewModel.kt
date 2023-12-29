package com.panelsense.app.ui.main

import androidx.lifecycle.viewModelScope
import com.panelsense.core.base.NavViewModel
import com.panelsense.data.icons.IconProvider
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
import javax.inject.Inject
import kotlin.reflect.KClass

@HiltViewModel
class MainViewModel @Inject constructor(
    private val loginInteractor: LoginInteractor,
    private val panelSenseInteractor: PanelSenseInteractor,
    private var iconProvider: IconProvider
) : NavViewModel<MainNavCommand, MainViewModel.MainViewState>(), EntityInteractor {

    override fun defaultState(): MainViewState = MainViewState()

    init {
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

    override fun <T : EntityState> listenOnState(entityId: String, kType: KClass<T>): Flow<T> {
        return panelSenseInteractor.observeEntityState(entityId)
            .mapNotNull { it as? T }

    }

    override fun sendCommand(command: EntityCommand) {
        panelSenseInteractor.sendCommand(command)
    }

    override fun getIconProvider(): IconProvider = iconProvider

    data class MainViewState(
        val panelConfiguration: Configuration? = null,
        val serverConnected: Boolean = true
    )
}
