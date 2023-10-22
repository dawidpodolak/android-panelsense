package com.panelsense.app.ui.login

sealed class LoginNavCommand {
    object NavigateToMain : LoginNavCommand()
}
