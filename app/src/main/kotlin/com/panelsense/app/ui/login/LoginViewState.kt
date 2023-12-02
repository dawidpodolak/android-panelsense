package com.panelsense.app.ui.login

data class LoginViewState(
    val isConnecting: Boolean = false,
    val addressText: String? = null,
    val portText: String? = null,
    val panelSenseNameText: String? = null,
    val userNameText: String? = null,
    val passwordText: String? = null,
)
