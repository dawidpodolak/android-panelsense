package com.panelsense.domain.model

data class ServerConnectionData(
    val serverIPAddress: String,
    val serverPort: String,
    val panelSenseName: String,
    val userName: String,
    val password: String
)
