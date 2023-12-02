package com.panelsense.app.ui.login

import androidx.lifecycle.viewModelScope
import com.panelsense.core.base.NavViewModel
import com.panelsense.domain.interactor.LoginInteractor
import com.panelsense.domain.model.ServerConnectionData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginInteractor: LoginInteractor
) : NavViewModel<LoginNavCommand, LoginViewState>() {

    override fun defaultState(): LoginViewState = LoginViewState()

    init {
        checkIfUserIsLoggedIn()
    }

    private fun checkIfUserIsLoggedIn() {
        viewModelScope.launch {
            val serverConnectionData = loginInteractor.getServerConnectionData()
            if (serverConnectionData != null) {
                modify {
                    copy(
                        addressText = serverConnectionData.serverIPAddress,
                        portText = serverConnectionData.serverPort,
                        panelSenseNameText = serverConnectionData.panelSenseName,
                        userNameText = serverConnectionData.userName,
                        passwordText = serverConnectionData.password
                    )
                }
            }
        }
    }

    fun login(serverConnectionData: ServerConnectionData) {
        viewModelScope.launch {
            modify { copy(isConnecting = true) }
            val result = loginInteractor.login(serverConnectionData)
            modify { copy(isConnecting = false) }
            when {
                result.isSuccess -> navigateTo(LoginNavCommand.NavigateToMain)
                result.isFailure -> {
                    showError(result.exceptionOrNull()!!)
                }
            }
        }
    }
}
